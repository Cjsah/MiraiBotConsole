package net.cjsah.console.command

import net.cjsah.console.command.Dispatcher.ResultConsumer
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.command.context.CommandContextBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.command.tree.LiteralCommandNode
import net.cjsah.console.command.tree.RootCommandNode
import net.cjsah.console.exceptions.CommandException
import java.util.*
import java.util.stream.Collectors

class Dispatcher {
    companion object {
        var ARGUMENT_SEPARATOR = ' '
        private const val USAGE_OPTIONAL_OPEN = "["
        private const val USAGE_OPTIONAL_CLOSE = "]"
        private const val USAGE_REQUIRED_OPEN = "("
        private const val USAGE_REQUIRED_CLOSE = ")"
        private const val USAGE_OR = "|"
    }

    private val roots = RootCommandNode()

    private var consumer = ResultConsumer { _, _, _ -> }

    fun register(command: LiteralArgumentBuilder): LiteralCommandNode {
        val build: LiteralCommandNode = command.build() as LiteralCommandNode
        roots.addChild(build)
        return build
    }

    @Throws(CommandException::class)
    internal fun execute(input: String, source: CommandSource<*>): Int {
        return execute(StringReader(input), source)
    }

    @Throws(CommandException::class)
    private fun execute(input: StringReader, source: CommandSource<*>): Int {
        val parse: ParseResults = parse(input, source)
        return execute(parse)
    }

    @Throws(CommandException::class)
    private fun execute(parse: ParseResults): Int {
        if (parse.reader.canRead()) {
            when {
                parse.exceptions.size == 1 -> throw parse.exceptions.values.iterator().next()
                parse.context.getRange().isEmpty() -> throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.reader)
                else -> throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.reader)
            }
        }
        var result = 0
        var foundCommand = false
        val original = parse.context.build()
        var contexts: List<CommandContext>? = listOf(original)
        var next: ArrayList<CommandContext>? = null
        while (contexts != null) {
            val size = contexts.size
            for (i in 0 until size) {
                val context = contexts[i]
                if (context.command != null) {
                    foundCommand = true
                    try {
                        context.command.run(context)
                        result += 1
                        consumer.onCommandComplete(context, true, 1)
                    } catch (ex: CommandException) {
                        consumer.onCommandComplete(context, false, 0)
                    }
                }
            }
            contexts = next
            next = null
        }
        if (!foundCommand) {
            consumer.onCommandComplete(original, false, 0)
            throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.reader)
        }
        return result
    }

    @Throws(CommandException::class)
    private fun parse(command: StringReader, source: CommandSource<*>): ParseResults {
        val context = CommandContextBuilder(this, source, roots, command.getCursor())
        return parseNodes(roots, command, context)
    }

    @Throws(CommandException::class)
    private fun parseNodes(node: CommandNode, originalReader: StringReader, contextSoFar: CommandContextBuilder): ParseResults {
        val source: CommandSource<*> = contextSoFar.getSource()
        var errors: MutableMap<CommandNode, CommandException>? = null
        var potentials: MutableList<ParseResults>? = null
        val cursor = originalReader.getCursor()
        for (child in node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue
            }
            val context: CommandContextBuilder = contextSoFar.copy()
            val reader = StringReader(originalReader)
            try {
                try {
                    child.parse(reader, context)
                } catch (e: RuntimeException) {
                    throw CommandException.BUILT_EXCEPTIONS.dispatcherParseException().createWithContext(reader, e.localizedMessage)
                }
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR) throw CommandException.BUILT_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader)
            } catch (e: CommandException) {
                if (errors == null) {
                    errors = LinkedHashMap<CommandNode, CommandException>()
                }
                errors[child] = e
                reader.setCursor(cursor)
                continue
            }
            context.withCommand(child.getCommand())
            if (reader.canRead(2)) {
                reader.skip()
                val parse = parseNodes(child, reader, context)
                if (potentials == null) {
                    potentials = ArrayList(1)
                }
                potentials.add(parse)
            } else {
                if (potentials == null) {
                    potentials = ArrayList(1)
                }
                potentials.add(ParseResults(context, reader, emptyMap()))
            }
        }
        if (potentials != null) {
            if (potentials.size > 1) {
                potentials.sortWith(Comparator { a, b ->
                    if (!a.reader.canRead() && b.reader.canRead()) {
                        return@Comparator -1
                    }
                    if (a.reader.canRead() && !b.reader.canRead()) {
                        return@Comparator 1
                    }
                    if (a.exceptions.isEmpty() && b.exceptions.isNotEmpty()) {
                        return@Comparator -1
                    }
                    if (a.exceptions.isNotEmpty() && b.exceptions.isEmpty()) {
                        return@Comparator 1
                    }
                    return@Comparator 0
                })
            }
            return potentials[0]
        }
        return ParseResults(contextSoFar, originalReader, errors ?: emptyMap())
    }

    fun getSmartUsage(source: CommandSource<*>): Map<CommandNode, String> {
        val result: MutableMap<CommandNode, String> = LinkedHashMap<CommandNode, String>()
        val optional = roots.getCommand() != null
        for (child in roots.getChildren()) {
            val usage: String? = getSmartUsage(child, source, optional, false)
            if (usage != null) {
                result[child] = usage
            }
        }
        return result
    }

    private fun getSmartUsage(node: CommandNode, source: CommandSource<*>, optional: Boolean, deep: Boolean): String? {
        if (!node.canUse(source)) {
            return null
        }
        val self: String =
            if (optional) USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE else node.getUsageText()
        val childOptional = node.getCommand() != null
        val open: String =
            if (childOptional) USAGE_OPTIONAL_OPEN else USAGE_REQUIRED_OPEN
        val close: String =
            if (childOptional) USAGE_OPTIONAL_CLOSE else USAGE_REQUIRED_CLOSE
        if (!deep) {
            val children: Collection<CommandNode> = node.getChildren().stream().filter { c -> c.canUse(source) }.collect(Collectors.toList())
            if (children.size == 1) {
                val usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional)
                if (usage != null) return self + ARGUMENT_SEPARATOR + usage
            } else if (children.size > 1) {
                val childUsage: MutableSet<String> = LinkedHashSet()
                for (child in children) {
                    val usage = getSmartUsage(child, source, childOptional, true)
                    if (usage != null) childUsage.add(usage)
                }
                if (childUsage.size == 1) {
                    val usage = childUsage.iterator().next()
                    return self + ARGUMENT_SEPARATOR + if (childOptional) USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE else usage
                } else if (childUsage.size > 1) {
                    val builder = StringBuilder(open)
                    var count = 0
                    for (child in children) {
                        if (count > 0) builder.append(USAGE_OR)
                        builder.append(child.getUsageText())
                        count++
                    }
                    if (count > 0) {
                        builder.append(close)
                        return self + ARGUMENT_SEPARATOR + builder.toString()
                    }
                }
            }
        }
        return self
    }

    fun interface ResultConsumer {
        fun onCommandComplete(context: CommandContext, success: Boolean, result: Int)
    }

}