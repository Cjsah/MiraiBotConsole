package net.cjsah.console.command.tree

import net.cjsah.console.command.Command
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.context.CommandContextBuilder
import net.cjsah.console.command.context.IntRange
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.exceptions.BuiltExceptions
import net.cjsah.console.exceptions.CommandException
import java.util.function.Predicate

class LiteralCommandNode(
    help: String,
    private val literal: String,
    command: Command?,
    requirement: Predicate<CommandSource<*>>
) : CommandNode(help, command, requirement) {
    override fun getName() = literal

    override fun getUsageText() = literal

    override fun isValidInput(input: String) = parse(StringReader(input)) > -1

    @Throws(CommandException::class)
    override fun parse(reader: StringReader, builder: CommandContextBuilder) {
        val start = reader.cursor
        val end = parse(reader)
        if (end < 0) throw BuiltExceptions.literalIncorrect().createWithContext(reader, literal)
        builder.withRange(IntRange(start, end))
    }

    override fun createBuilder(): ArgumentBuilder<*> {
        return LiteralArgumentBuilder.literal(literal).apply {
            this.requires(super.getRequirement())
            if (super.getCommand() != null) this.executes(super.getHelp(), super.getCommand())
        }
    }

    override fun getSortedKey() = literal

    private fun parse(reader: StringReader): Int {
        if (reader.canRead(literal.length)) {
            val start = reader.cursor
            val end = start + literal.length
            if (reader.string.substring(start, end) == literal) {
                reader.cursor = end
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end
                } else {
                    reader.cursor = start
                }
            }
        }
        return -1
    }

}