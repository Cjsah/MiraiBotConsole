package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.builder.RequiredArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.context.ParsedResult
import net.cjsah.bot.console.command.exceptions.CommandException
import java.util.function.Predicate

class ArgumentCommandNode<S, T>(
    private val name: String,
    private val type: ArgumentType<T>,
    command: Command<S>?,
    requirement: Predicate<S>
) : CommandNode<S>(command, requirement) {

    fun getType() = type

    override fun getName() = name

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>) {
        val start = reader.getCursor()
        val result: T = type.parse(reader)
        val parsed: ParsedResult<T> = ParsedResult(start, reader.getCursor(), result)

        contextBuilder.withArgument(name, parsed)
        contextBuilder.withNode(this, parsed.getRange())

    }

    override fun isValidInput(input: String): Boolean {
        return try {
            val reader = StringReader(input)
            type.parse(reader)
            !reader.canRead() || reader.peek() == ' '
        } catch (ignored: CommandException) {
            false
        }
    }

    override fun createBuilder(): ArgumentBuilder<S, *> {
        val builder: RequiredArgumentBuilder<S, T> = RequiredArgumentBuilder.argument(name, type)
        builder.requires(getRequirement())
        if (getCommand() != null) {
            builder.executes(getCommand())
        }
        return builder
    }

    override fun getSortedKey() = name

}