@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.exceptions.CommandException
import java.util.function.Predicate

abstract class CommandNode<S>(
    private var command: Command<S>?,
    private val requirement: Predicate<S>
) : Comparable<CommandNode<S>> {
    private val children: Map<String, CommandNode<S>> = LinkedHashMap()
    private val literals = LinkedHashMap<String, LiteralCommandNode<S>>()
    private val arguments = LinkedHashMap<String, ArgumentCommandNode<S, *>>()

    fun getCommand(): Command<S>? {
        return command
    }

    fun getChildren(): Collection<CommandNode<S>> {
        return children.values
    }

    fun getChild(name: String): CommandNode<S>? {
        return children[name]
    }

    open fun canUse(source: S): Boolean {
        return requirement.test(source)
    }

    open fun addChild(node: CommandNode<S>) {
        if (node is RootCommandNode) {
            throw UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode")
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
            } else if (node is ArgumentCommandNode<S, *>) {
                arguments[node.getName()] = node
            }
        }
    }

    protected abstract fun isValidInput(input: String): Boolean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandNode<*>) return false

        if (children != other.children) return false
        return !if (command != null) command!! != other.command else other.command != null
    }

    override fun hashCode(): Int {
        return 31 * children.hashCode() + if (command != null) command.hashCode() else 0
    }

    fun getRequirement(): Predicate<S> {
        return requirement
    }

    abstract fun getName(): String

    @Throws(CommandException::class)
    abstract fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>)

    abstract fun createBuilder(): ArgumentBuilder<S, *>

    protected abstract fun getSortedKey(): String

    override fun compareTo(other: CommandNode<S>): Int {
        if (this is LiteralCommandNode == other is LiteralCommandNode) {
            return getSortedKey().compareTo(other.getSortedKey())
        }
        return if (other is LiteralCommandNode) 1 else -1
    }
}