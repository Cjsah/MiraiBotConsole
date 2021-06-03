package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.tree.CommandNode

class CommandContext(
    private val source: CommandSource,
    private val input: String,
    private val arguments: Map<String, ParsedNodeResult<*>>,
    private val command: Command?,
    private val rootNode: CommandNode,
    private val nodes: List<ParsedNode>,
    private val range: IntRange,
    private val child: CommandContext?
) {

    fun copyFor(source: CommandSource): CommandContext {
        return if (this.source === source) {
            this
        } else CommandContext(source, input, arguments, command, rootNode, nodes, range, child)
    }

    fun getLastChild(): CommandContext? {
        var result: CommandContext? = this
        while (result?.getChild() != null) {
            result = result.getChild()
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    fun <V> getArgument(name: String, clazz: Class<V>): V {
        val argument = arguments[name] ?: throw IllegalArgumentException("No such argument '$name' exists on this command")
        val result = argument.result!!
        return if (clazz.isAssignableFrom(result.javaClass)) {
            result as V
        } else {
            throw IllegalArgumentException("Argument '" + name + "' is defined as " + result.javaClass.simpleName + ", not " + clazz)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandContext) return false
        val that: CommandContext = other
        if (arguments != that.arguments) return false
        if (rootNode != that.rootNode) return false
        if (nodes.size != that.nodes.size || nodes != that.nodes) return false
        if (if (command != null) command != that.command else that.command != null) return false
        if (source != that.source) return false
        return !if (child != null) child != that.child else that.child != null
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + arguments.hashCode()
        result = 31 * result + (command?.hashCode() ?: 0)
        result = 31 * result + rootNode.hashCode()
        result = 31 * result + nodes.hashCode()
        result = 31 * result + (child?.hashCode() ?: 0)
        return result
    }

    fun getChild() = child

    fun getCommand() = command

    fun getSource() = source

    fun getRange() = range

    fun getInput() = input

    fun getRootNode() = rootNode

    fun getNodes() = nodes

    fun hasNodes() = nodes.isNotEmpty()

}