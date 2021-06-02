package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import java.io.StringReader
import java.util.function.Predicate

class ArgumentCommandNode<S, T>(
    name: String,
    type: ArgumentType,
    command: Command<S>?,
    requirement: Predicate<S>
) : CommandNode<S>(command, requirement) {

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun isValidInput(input: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>) {
        TODO("Not yet implemented")
    }

    override fun createBuilder(): ArgumentBuilder<S, *> {
        TODO("Not yet implemented")
    }

    override fun getSortedKey(): String {
        TODO("Not yet implemented")
    }
}