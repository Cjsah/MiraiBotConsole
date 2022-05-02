@file:Suppress("PrivatePropertyName", "unused")
@file:JvmBlockingBridge

package net.cjsah.console.command

import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import net.cjsah.console.Console
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.command.tree.RootCommandNode
import net.cjsah.console.exceptions.BuiltExceptions
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.exceptions.PluginException
import net.cjsah.console.plugin.Plugin
import net.cjsah.console.text.TranslateText

class Dispatcher {
    companion object {
        private const val ARGUMENT_SEPARATOR = ' '
    }
    private val ROOTS = RootCommandNode()

    fun register(command: LiteralArgumentBuilder){
        if (Console.isFrozen())
            throw ConsoleException.create(TranslateText("command.reg.freezed"), PluginException::class.java)
        ROOTS.addChild(command.build())
    }

    fun register(plugin: Plugin, command: LiteralArgumentBuilder) = ROOTS.addChild(plugin, command.build())

    fun deregister(plugin: Plugin) = ROOTS.removeChild(plugin)

    @Throws(CommandException::class)
    suspend fun execute(input: String, source: CommandSource<*>): Int {
        val reader = StringReader(input)
        val builder = ContextBuilder(this, source, reader.getCursor())
        val parse = parseNodes(ROOTS, reader, builder)
        if (parse.reader.canRead()) {
            if (parse.exceptions.size == 1) throw parse.exceptions.values.iterator().next()
            else if (parse.context.getRange().isEmpty())
                throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.createWithContext(reader)
            else throw BuiltExceptions.DISPATCHER_UNKNOWN_ARGUMENT.createWithContext(reader)
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
        if (!foundCommand) throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.createWithContext(reader)
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
                    throw BuiltExceptions.DISPATCHER_PARSE_EXCEPTION.createWithContext(reader, e.localizedMessage)
                }
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR)
                    throw BuiltExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR.createWithContext(reader)
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

    private fun getHelp(result: MutableMap<String, String>, prefix: String, node: CommandNode, source: CommandSource<*>, first: Boolean) {
        var command = prefix
        if (!node.canUse(source)) return
        command += "${if (first) "" else " "}${node.getUsageText()}"
        if (node.getCommand() != null) {
            result[command] = node.getHelp()
        }
        node.getChildren().forEach { this.getHelp(result, command, it, source, false) }
    }

}