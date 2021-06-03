package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.Dispatcher.ResultConsumer
import net.cjsah.bot.console.command.builder.LiteralArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.exceptions.CommandException
import net.cjsah.bot.console.command.tree.CommandNode
import net.cjsah.bot.console.command.tree.LiteralCommandNode
import net.cjsah.bot.console.command.tree.RootCommandNode
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Dispatcher {
    companion object {
        var ARGUMENT_SEPARATOR = ' '
    }

    private val roots = HashMap<SourceType, RootCommandNode>()

    private var consumer = ResultConsumer { _, _, _ -> }

    init {
        roots[SourceType.CONSOLE] = RootCommandNode()
        roots[SourceType.MEMBER] = RootCommandNode()
    }

    private fun hasCommand(): Predicate<CommandNode> = Predicate<CommandNode> {
        it != null && (it.getCommand() != null || it.getChildren().stream().anyMatch(hasCommand()))
    }

    fun register(type: SourceType, command: LiteralArgumentBuilder): LiteralCommandNode {
        val build: LiteralCommandNode = command.build() as LiteralCommandNode
        roots[type]!!.addChild(build)
        return build
    }

    fun setConsumer(consumer: ResultConsumer) {
        this.consumer = consumer
    }

    @Throws(CommandException::class)
    internal fun execute(input: String, source: CommandSource): Int {
        return execute(StringReader(input), source)
    }

    @Throws(CommandException::class)
    fun execute(input: StringReader, source: CommandSource): Int {
        val parse: ParseResults = parse(input, source)
        return execute(parse)
    }

    @Throws(CommandException::class)
    fun execute(parse: ParseResults): Int {
        if (parse.getReader().canRead()) {
            when {
                parse.getExceptions().size == 1 -> throw parse.getExceptions().values.iterator().next()
                parse.getContext().getRange().isEmpty() -> throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader())
                else -> throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader())
            }
        }
        var result = 0
        var foundCommand = false
        val command = parse.getReader().getString()
        val original = parse.getContext().build(command)
        var contexts: List<CommandContext>? = listOf(original)
        var next: ArrayList<CommandContext>? = null
        while (contexts != null) {
            val size = contexts.size
            for (i in 0 until size) {
                val context = contexts[i]
                val child = context.getChild()
                if (child != null) {
                    if (child.hasNodes()) {
                        foundCommand = true
                        if (next == null) {
                            next = ArrayList(1)
                        }
                        next.add(child.copyFor(context.getSource()))
                    }
                } else if (context.getCommand() != null) {
                    foundCommand = true
                    try {
                        context.getCommand()!!.run(context)
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
            throw CommandException.BUILT_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader())
        }
        return result
    }

    fun parse(command: String, source: CommandSource) = parse(StringReader(command), source)

    private fun parse(command: StringReader, source: CommandSource): ParseResults {
        val root = roots[source.source]!!
        val context = CommandContextBuilder(this, source, root, command.getCursor())
        return parseNodes(root, command, context)
    }

    private fun parseNodes(node: CommandNode, originalReader: StringReader, contextSoFar: CommandContextBuilder): ParseResults {
        val source: CommandSource = contextSoFar.getSource()
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
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return@Comparator -1
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return@Comparator 1
                    }
                    if (a.getExceptions().isEmpty() && b.getExceptions().isNotEmpty()) {
                        return@Comparator -1
                    }
                    if (a.getExceptions().isNotEmpty() && b.getExceptions().isEmpty()) {
                        return@Comparator 1
                    }
                    return@Comparator 0
                })
            }
            return potentials[0]
        }
        return ParseResults(contextSoFar, originalReader, errors ?: emptyMap())
    }

    fun getRoots() = roots

    fun interface ResultConsumer {
        fun onCommandComplete(context: CommandContext, success: Boolean, result: Int)
    }

}