package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.builder.RequiredArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.context.ParsedNodeResult
import net.cjsah.bot.console.command.exceptions.CommandException
import java.util.function.Predicate

class ArgumentCommandNode<T>(
    private val name: String,
    private val type: ArgumentType<T>,
    command: Command?,
    requirement: Predicate<CommandSource>
) : CommandNode(command, requirement) {

    fun getType() = type

    override fun getName() = name

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder) {
        val start = reader.getCursor()
        val result: T = type.parse(reader)
        val parsed: ParsedNodeResult<T> = ParsedNodeResult(start, reader.getCursor(), result)

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

    override fun createBuilder(): ArgumentBuilder<*> {
        val builder: RequiredArgumentBuilder<T> = RequiredArgumentBuilder.argument(name, type)
        builder.requires(getRequirement())
        if (getCommand() != null) {
            builder.executes(getCommand())
        }
        return builder
    }

    override fun getSortedKey() = name

}