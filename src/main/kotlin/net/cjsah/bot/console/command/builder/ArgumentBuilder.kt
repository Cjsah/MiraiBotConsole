package net.cjsah.bot.console.command.builder

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.source.CommandSource
import net.cjsah.bot.console.command.tree.CommandNode
import net.cjsah.bot.console.command.tree.RootCommandNode
import java.util.function.Predicate

abstract class ArgumentBuilder<T : ArgumentBuilder<T>> {
    private val arguments: RootCommandNode = RootCommandNode()
    private var command: Command? = null
    private var requirement = Predicate<CommandSource<*>> { true }
    private val target: CommandNode? = null

    internal abstract fun getThis(): T

    fun then(argument: ArgumentBuilder<*>): T {
        check(target == null) { "无法将子节点添加到重定向节点" }
        arguments.addChild(argument.build())
        return getThis()
    }

    fun getArguments(): Collection<CommandNode> {
        return arguments.getChildren()
    }

    fun executes(command: Command?): T {
        this.command = command
        return getThis()
    }

    fun getCommand(): Command? {
        return command
    }

    fun requires(requirement: Predicate<CommandSource<*>>): T {
        this.requirement = requirement
        return getThis()
    }

    fun getRequirement(): Predicate<CommandSource<*>> {
        return requirement
    }

    abstract fun build(): CommandNode
}