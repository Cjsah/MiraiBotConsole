@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.source.CommandSource
import net.cjsah.bot.console.exceptions.CommandException
import java.util.function.Predicate

abstract class CommandNode(
    private var command: Command?,
    private val requirement: Predicate<CommandSource<*>>
) : Comparable<CommandNode> {
    private val children: Map<String, CommandNode> = LinkedHashMap()
    private val literals = LinkedHashMap<String, LiteralCommandNode>()
    private val arguments = LinkedHashMap<String, ArgumentCommandNode<*>>()

    fun getCommand(): Command? {
        return command
    }

    fun getChildren(): Collection<CommandNode> {
        return children.values
    }

    open fun canUse(source: CommandSource<*>): Boolean {
        return requirement.test(source)
    }

    open fun addChild(node: CommandNode) {
        if (node is RootCommandNode) {
            throw UnsupportedOperationException("无法将 'RootCommandNode' 作为一个子节点添加到其他 'CommandNode'")
        }
        val child = children[node.getName()]
        if (child != null) {
            // We've found something to merge onto
            if (node.getCommand() != null) {
                child.command = node.getCommand()
            }
            for (grandchild in node.getChildren()) {
                child.addChild(grandchild)
            }
        } else {
            (children as LinkedHashMap)[node.getName()] = node
            if (node is LiteralCommandNode) {
                literals[node.getName()] = node
            } else if (node is ArgumentCommandNode<*>) {
                arguments[node.getName()] = node
            }
        }
    }

    abstract fun getUsageText(): String

    protected abstract fun isValidInput(input: String): Boolean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandNode) return false

        if (children != other.children) return false
        return !if (command != null) command!! != other.command else other.command != null
    }

    override fun hashCode(): Int {
        return 31 * children.hashCode() + if (command != null) command.hashCode() else 0
    }

    fun getRequirement(): Predicate<CommandSource<*>> {
        return requirement
    }

    abstract fun getName(): String

    @Throws(CommandException::class)
    abstract fun parse(reader: StringReader, contextBuilder: CommandContextBuilder)

    abstract fun createBuilder(): ArgumentBuilder<*>

    protected abstract fun getSortedKey(): String

    open fun getRelevantNodes(input: StringReader): Collection<CommandNode> {
        return if (literals.size > 0) {
            val cursor = input.getCursor()
            while (input.canRead() && input.peek() != ' ') {
                input.skip()
            }
            val text = input.getString().substring(cursor, input.getCursor())
            input.setCursor(cursor)
            val literal = literals[text]
            literal?.let { setOf(it) } ?: arguments.values
        } else {
            arguments.values
        }
    }

    override fun compareTo(other: CommandNode): Int {
        if (this is LiteralCommandNode == other is LiteralCommandNode) {
            return getSortedKey().compareTo(other.getSortedKey())
        }
        return if (other is LiteralCommandNode) 1 else -1
    }
}