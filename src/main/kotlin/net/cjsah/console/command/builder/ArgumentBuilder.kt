package net.cjsah.console.command.builder

import net.cjsah.console.command.Command
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.command.tree.RootCommandNode
import java.util.function.Predicate

abstract class ArgumentBuilder<T : ArgumentBuilder<T>> {
    private val arguments = RootCommandNode()
    private var requirement: Predicate<CommandSource<*>> = Predicate { true }
    private var command: Command? = null
    private var help = ""

    open fun then(argument: ArgumentBuilder<*>): T {
        arguments.addChild(argument.build())
        return getThis()
    }

    open fun executes(help: String, command: Command?): T {
        this.command = command
        this.help = help
        return getThis()
    }

    open fun requires(requirement: Predicate<CommandSource<*>>): T {
        this.requirement = requirement
        return getThis()
    }

    protected abstract fun getThis(): T

    abstract fun build(): CommandNode

    open fun getArguments(): Collection<CommandNode> {
        return arguments.getChildren()
    }

    open fun getRequirement(): Predicate<CommandSource<*>> {
        return requirement
    }

    open fun getCommand(): Command? {
        return command
    }

    open fun getHelp(): String {
        return help
    }

}