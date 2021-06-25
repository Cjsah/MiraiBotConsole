package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.builder.LiteralArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.exceptions.CommandException
import net.cjsah.bot.console.command.source.CommandSource
import java.util.function.Predicate

class LiteralCommandNode(
    private val literal: String,
    command: Command?,
    requirement: Predicate<CommandSource<*>>
) : CommandNode(command, requirement) {

    fun getLiteral() = literal

    override fun getName() = literal

    @Throws(CommandException::class)
    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder) {
        val start: Int = reader.getCursor()
        val end: Int = parse(reader)
        if (end > -1) {
            contextBuilder.withRange(IntRange(start, end))
            return
        }

        throw CommandException.BUILT_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal)
    }

    private fun parse(reader: StringReader): Int {
        val start: Int = reader.getCursor()
        if (reader.canRead(literal.length)) {
            val end = start + literal.length
            if (reader.getString().substring(start, end) == literal) {
                reader.setCursor(end)
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end
                } else {
                    reader.setCursor(start)
                }
            }
        }
        return -1
    }

    override fun isValidInput(input: String) = parse(StringReader(input)) > -1


    override fun createBuilder(): ArgumentBuilder<*> {
        val builder = LiteralArgumentBuilder.literal(literal)
        builder.requires(getRequirement())
        if (getCommand() != null) {
            builder.executes(getCommand())
        }
        return builder

    }

    override fun getSortedKey() = literal
}