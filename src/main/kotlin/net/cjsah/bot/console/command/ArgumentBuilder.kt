package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.argument.ArgumentType
import java.util.Collections
import java.util.function.Predicate
import kotlin.collections.ArrayList

class ArgumentBuilder(val target: ArgumentType) {
    private val arguments: Collection<ArgumentBuilder> = Collections.emptyList()
    private var requirement: Predicate<CommandSource> = Predicate<CommandSource> { true }
    private var command: Command? = null


    private fun getThis() = this

    fun then(argument: ArgumentBuilder): ArgumentBuilder {
        (this.arguments as ArrayList).add(argument)
        return getThis()
    }

    fun executes(command: Command): ArgumentBuilder {
        this.command = command
        return getThis()
    }

    fun getNext(content: String): ArgumentBuilder? {
        this.arguments.forEach {
            if (it.target.isThisCommand(content)) return it
        }
        return null
    }

    fun runCommand(source: CommandSource) {
        this.command?.run(source) ?: throw CommandException.UNKNOWN_COMMAND
    }

    fun requires(requirement: Predicate<CommandSource>): ArgumentBuilder {
        this.requirement = requirement
        return getThis()
    }

    fun isRequirement(source: CommandSource) = this.requirement.test(source)

    interface Command {
        fun run(context: CommandSource)
    }
}