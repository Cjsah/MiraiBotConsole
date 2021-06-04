package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.Dispatcher
import net.cjsah.bot.console.command.tree.CommandNode
import kotlin.math.max
import kotlin.math.min

class CommandContextBuilder(
    private val dispatcher: Dispatcher,
    private var source: CommandSource,
    private val rootNode: CommandNode,
    start: Int
) {
    private val arguments = LinkedHashMap<String, ParsedNodeResult<*>>()
    private var command: Command? = null
    private var range: IntRange = IntRange(start, start)

    fun withArgument(name: String, argument: ParsedNodeResult<*>): CommandContextBuilder {
        arguments[name] = argument
        return this
    }

    fun withCommand(command: Command?): CommandContextBuilder {
        this.command = command
        return this
    }

    fun withRange(range: IntRange): CommandContextBuilder {
        this.range = encompassing(this.range, range)
        return this
    }

    fun copy(): CommandContextBuilder {
        val copy = CommandContextBuilder(dispatcher, source, rootNode, range.first)
        copy.command = command
        copy.arguments.putAll(arguments)
        copy.range = range
        return copy
    }

    fun build(): CommandContext {
        return CommandContext(arguments, command)
    }

    fun getSource() = source

    fun getRange() = range

    private fun encompassing(a: IntRange, b: IntRange) = IntRange(min(a.first, b.first), max(a.last, b.last))

}