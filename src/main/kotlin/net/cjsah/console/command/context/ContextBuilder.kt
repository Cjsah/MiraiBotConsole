@file:Suppress("unused")

package net.cjsah.console.command.context

import net.cjsah.console.command.Command
import net.cjsah.console.command.Dispatcher
import net.cjsah.console.command.source.CommandSource

class ContextBuilder(
    private val dispatcher: Dispatcher,
    private val source: CommandSource<*>,
    start: Int
) {
    private val arguments: MutableMap<String, ParsedNode<*>> = LinkedHashMap()
    private var command: Command? = null
    private var range: Range

    init {
        range = Range(start)
    }

    fun withArgument(name: String, argument: ParsedNode<*>): ContextBuilder {
        arguments[name] = argument
        return this
    }

    fun withCommand(command: Command?): ContextBuilder {
        this.command = command
        return this
    }

    fun withRange(range: Range): ContextBuilder {
        this.range = Range(this.range, range)
        return this
    }

    fun copy(): ContextBuilder {
        val builder = ContextBuilder(dispatcher, source, range.start)
        builder.command = command
        builder.arguments.putAll(arguments)
        builder.range = range
        return builder
    }

    fun build(): CommandContext {
        return CommandContext(arguments, source, command)
    }

    fun getSource(): CommandSource<*> {
        return source
    }

    fun getRange(): Range {
        return range
    }

}