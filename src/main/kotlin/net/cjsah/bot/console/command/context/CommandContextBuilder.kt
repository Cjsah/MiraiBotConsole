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
    private val nodes = ArrayList<ParsedNode>()
    private var command: Command? = null
    private var child: CommandContextBuilder? = null
    private var range: IntRange = IntRange(start, start)

    fun withSource(source: CommandSource): CommandContextBuilder {
        this.source = source
        return this
    }

    fun withArgument(name: String, argument: ParsedNodeResult<*>): CommandContextBuilder {
        arguments[name] = argument
        return this
    }

    fun withCommand(command: Command?): CommandContextBuilder {
        this.command = command
        return this
    }

    fun withNode(node: CommandNode, range: IntRange): CommandContextBuilder {
        nodes.add(ParsedNode(node, range))
        this.range = encompassing(this.range, range)
        return this
    }

    fun copy(): CommandContextBuilder {
        val copy = CommandContextBuilder(dispatcher, source, rootNode, range.first)
        copy.command = command
        copy.arguments.putAll(arguments)
        copy.nodes.addAll(nodes)
        copy.child = child
        copy.range = range
        return copy
    }

    fun withChild(child: CommandContextBuilder?): CommandContextBuilder {
        this.child = child
        return this
    }

    fun getLastChild(): CommandContextBuilder? {
        var result: CommandContextBuilder? = this
        while (result?.getChild() != null) {
            result = result.getChild()
        }
        return result
    }

    fun build(input: String): CommandContext {
        return CommandContext(source, input, arguments, command, rootNode, nodes, range, child?.build(input))
    }

    fun getSource() = source

    fun getRootNode() = rootNode

    fun getArguments() = arguments

    fun getChild() = child

    fun getCommand() = command

    fun getNodes() = nodes

    fun getDispatcher() = dispatcher

    fun getRange() = range

    private fun encompassing(a: IntRange, b: IntRange) = IntRange(min(a.first, b.first), max(a.last, b.last))

}