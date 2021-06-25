package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.Dispatcher.ResultConsumer
import net.cjsah.bot.console.command.builder.LiteralArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.exceptions.CommandException
import net.cjsah.bot.console.command.source.CommandSource
import net.cjsah.bot.console.command.tree.CommandNode
import net.cjsah.bot.console.command.tree.LiteralCommandNode
import net.cjsah.bot.console.command.tree.RootCommandNode
import java.util.*
import kotlin.collections.ArrayList

class Dispatcher {
    companion object {
        var ARGUMENT_SEPARATOR = ' '
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

    fun interface ResultConsumer {
        fun onCommandComplete(context: CommandContext, success: Boolean, result: Int)
    }

}