@file:Suppress("PrivatePropertyName")

package net.cjsah.console.command

import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.command.tree.RootCommandNode
import net.cjsah.console.exceptions.BuiltExceptions
import net.cjsah.console.exceptions.CommandException

class Dispatcher {
    companion object {
        private const val ARGUMENT_SEPARATOR = ' '
    }
    private val ROOTS = RootCommandNode()

    fun register(command: LiteralArgumentBuilder) = ROOTS.addChild(command.build())

    @Throws(CommandException::class)
    fun execute(input: String, source: CommandSource<*>): Int {
        val reader = StringReader(input)
        val builder = ContextBuilder(this, source, ROOTS, reader.getCursor())
        val parse = parseNodes(ROOTS, reader, builder)
        if (parse.reader.canRead()) {
            if (parse.exceptions.size == 1) throw parse.exceptions.values.iterator().next()
            else if (parse.context.getRange().isEmpty()) throw BuiltExceptions.dispatcherUnknownCommand().createWithContext(reader)
            else throw BuiltExceptions.dispatcherUnknownArgument().createWithContext(reader)
        }
        var result = 0
        var foundCommand = false
        val original = parse.context.build()
        var contexts: List<CommandContext>? = listOf(original)
        while (contexts != null) {
            contexts.forEach {
                it.getCommand()?.let { command ->
                    foundCommand = true
                    try {
                        result += command.run(it)
                    } catch (e: CommandException) {
                        e.printStackTrace()
                    }
                }
            }
            contexts = null
        }
        if (!foundCommand) throw BuiltExceptions.dispatcherUnknownCommand().createWithContext(reader)
        return result
    }

    private fun parseNodes(node: CommandNode, originalReader: StringReader, builder: ContextBuilder): ParseResults {
        val exceptions: MutableMap<CommandNode, CommandException> = LinkedHashMap()
        val source = builder.getSource()
        val potentials: MutableList<ParseResults> = ArrayList()
        val cursor = originalReader.getCursor()
        for (child in node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) continue
            val context = builder.copy()
            val reader = originalReader.copy()
            try {
                try {
                    child.parse(reader, context)
                } catch (e: CommandException) {
                    throw BuiltExceptions.dispatcherParseException().createWithContext(reader, e.localizedMessage)
                }
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR) throw BuiltExceptions.dispatcherExpectedArgumentSeparator().createWithContext(reader)
            } catch (e: CommandException) {
                exceptions[child] = e
                reader.setCursor(cursor)
                continue
            }
            context.withCommand(child.getCommand())
            if (reader.canRead(2)) {
                reader.skip()
                val parse = parseNodes(child, reader, context)
                potentials.add(parse)
            } else {
                potentials.add(ParseResults(context, reader, emptyMap()))
            }
        }
        if (potentials.isNotEmpty()) {
            if (potentials.size > 1) {
                potentials.sortWith(Comparator { a: ParseResults, b: ParseResults ->
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
                    0
                })
            }
            return potentials[0]
        }
        return ParseResults(builder, originalReader, exceptions)
    }

    fun getHelp(source: CommandSource<*>): Map<String, String> {
        val result: MutableMap<String, String> = LinkedHashMap()
        ROOTS.getChildren().forEach { this.getHelp(result, "/", it, source, true) }
        return result
    }

    private fun getHelp(result: MutableMap<String, String>, mem: String, node: CommandNode, source: CommandSource<*>, first: Boolean) {
        var finalMem = mem
        if (!node.canUse(source)) return
        finalMem += "${if (first) "" else " "}${node.getUsageText()}"
        if (node.getCommand() != null) {
            result[finalMem] = node.getHelp()
        }
        node.getChildren().forEach { this.getHelp(result, finalMem, it, source, false) }
    }

}