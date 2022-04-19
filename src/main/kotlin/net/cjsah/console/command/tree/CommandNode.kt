package net.cjsah.console.command.tree

import net.cjsah.console.command.Command
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.plugin.Plugin
import java.util.function.Predicate

abstract class CommandNode(
    private var help: String,
    private var command: Command?,
    private val requirement: Predicate<CommandSource<*>>
) : Comparable<CommandNode> {
    private val children: MutableMap<String, CommandNode> = LinkedHashMap()
    private val literals: MutableMap<String, LiteralCommandNode> = LinkedHashMap()
    private val arguments: MutableMap<String, ArgumentCommandNode<*>> = LinkedHashMap()
    private var plugin: Plugin? = null

    abstract fun getName(): String

    abstract fun getUsageText(): String

    protected abstract fun isValidInput(input: String): Boolean

    @Throws(CommandException::class)
    abstract fun parse(reader: StringReader, builder: ContextBuilder)

    abstract fun createBuilder(): ArgumentBuilder<*>

    protected abstract fun getSortedKey(): String

    open fun getChildren(): Collection<CommandNode> {
        return children.values
    }

    open fun getCommand(): Command? {
        return command
    }

    open fun getHelp(): String {
        return help
    }

    open fun getRequirement(): Predicate<CommandSource<*>> {
        return requirement
    }

    open fun canUse(source: CommandSource<*>): Boolean {
        return requirement.test(source)
    }

    internal fun setPlugin(plugin: Plugin) {
        this.plugin = plugin
    }

    open fun addChild(node: CommandNode) {
        if (node is RootCommandNode)
            throw UnsupportedOperationException("无法将 'RootCommandNode' 作为一个子节点添加到其他 'CommandNode'")
        children[node.getName()]?.let { child ->
            child.help = node.help
            node.command?.let { child.command = it }
            node.getChildren().forEach { child.addChild(it) }
        } ?: let {
            children[node.getName()] = node
            if (node is LiteralCommandNode) {
                literals[node.getName()] = node
            } else if (node is ArgumentCommandNode<*>) {
                arguments[node.getName()] = node
            }
        }
    }

    open fun removeChild(plugin: Plugin) {
        children.entries.removeIf {
            if (it.value.plugin == plugin){
                literals.remove(it.key)
                arguments.remove(it.key)
                true
            } else false
        }
    }

    open fun getRelevantNodes(input: StringReader): Collection<CommandNode> {
        if (literals.isNotEmpty()) {
            val cursor = input.getCursor()
            while (input.canRead() && input.peek() != ' ') input.skip()
            val text = input.getString().substring(cursor, input.getCursor())
            input.setCursor(cursor)
            val literal = literals[text]
            return literal?.let { listOf(it) } ?: arguments.values
        }
        return arguments.values
    }

    override fun hashCode(): Int {
        return 31 * children.hashCode() + if (command != null) command.hashCode() else 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandNode) return false
        return if (children !== other.children) false else !if (command != null) command !== other.command else other.command != null
    }

    override fun compareTo(other: CommandNode): Int {
        return if (this is LiteralCommandNode == other is LiteralCommandNode)
            getSortedKey().compareTo(other.getSortedKey())
        else if (other is LiteralCommandNode) 1
        else -1
    }

}