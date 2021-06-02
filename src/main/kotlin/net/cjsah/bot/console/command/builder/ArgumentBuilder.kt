package net.cjsah.bot.console.command.builder

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.tree.CommandNode
import net.cjsah.bot.console.command.tree.RootCommandNode
import java.util.function.Predicate

abstract class ArgumentBuilder<S, T : ArgumentBuilder<S, T>> {
    private val arguments: RootCommandNode<S> = RootCommandNode()
    private var command: Command<S>? = null
    private var requirement = Predicate<S> { true }
    private val target: CommandNode<S>? = null

    internal abstract fun getThis(): T

    fun then(argument: ArgumentBuilder<S, *>): T {
        check(target == null) { "Cannot add children to a redirected node" }
        arguments.addChild(argument.build())
        return getThis()
    }

    fun getArguments(): Collection<CommandNode<S>> {
        return arguments.getChildren()
    }

    fun executes(command: Command<S>): T {
        this.command = command
        return getThis()
    }

    fun getCommand(): Command<S>? {
        return command
    }

    fun requires(requirement: Predicate<S>): T {
        this.requirement = requirement
        return getThis()
    }

    fun getRequirement(): Predicate<S> {
        return requirement
    }

    abstract fun build(): CommandNode<S>
}