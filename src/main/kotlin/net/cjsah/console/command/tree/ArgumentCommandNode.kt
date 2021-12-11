package net.cjsah.console.command.tree

import net.cjsah.console.command.Command
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.argument.Argument
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.builder.RequiredArgumentBuilder
import net.cjsah.console.command.context.CommandContextBuilder
import net.cjsah.console.command.context.ParsedNodeResult
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.exceptions.CommandException
import java.util.function.Predicate

class ArgumentCommandNode<T> (
    help: String,
    private val name: String,
    private val type: Argument<T>,
    command: Command?,
    requirement: Predicate<CommandSource<*>>

) : CommandNode(help, command, requirement) {
    override fun getName() = name

    override fun getUsageText() = "<$name>"

    override fun isValidInput(input: String): Boolean {
        return try {
            val reader = StringReader(input)
            type.parse(reader)
            !reader.canRead() || reader.peek() == ' '
        } catch (ignored: CommandException) {
            false
        }
    }

    override fun parse(reader: StringReader, builder: CommandContextBuilder) {
        val start = reader.cursor
        val result = type.parse(reader)
        val parsed = ParsedNodeResult(start, reader.cursor, result)
        builder.withArgument(name, parsed).withRange(parsed.range)
    }

    override fun createBuilder(): ArgumentBuilder<*> {
        return RequiredArgumentBuilder.argument(name, type).apply {
            this.requires(super.getRequirement())
            if (super.getCommand() != null) this.executes(super.getHelp(), super.getCommand())
        }
    }

    override fun getSortedKey() = name
}