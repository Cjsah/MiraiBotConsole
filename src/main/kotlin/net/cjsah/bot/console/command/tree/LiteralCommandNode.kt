package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.exceptions.CommandException
import java.util.*
import java.util.function.Predicate

class LiteralCommandNode<S>(
    private val literal: String,
    command: Command<S>?,
    requirement: Predicate<S>
) : CommandNode<S>(command, requirement) {
    private val literalLowerCase = literal.toLowerCase(Locale.ROOT)

    fun getLiteral(): String {
        return literal
    }

    override fun getName(): String {
        return literal
    }

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>) {
        val start: Int = reader.getCursor()
        val end: Int = parse(reader)
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end))
            return
        }

        throw CommandException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal)
    }

    private fun parse(reader: StringReader): Int {
        val start: Int = reader.getCursor()
        if (reader.canRead(literal.length)) {
            val end = start + literal.length
            if (reader.getString().substring(start, end).equals(literal)) {
                reader.setCursor(end)
                if (!reader.canRead() || reader.peek() === ' ') {
                    return end
                } else {
                    reader.setCursor(start)
                }
            }
        }
        return -1
    }

    override fun isValidInput(input: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createBuilder(): ArgumentBuilder<S, *> {
        TODO("Not yet implemented")
    }

    override fun getSortedKey(): String {
        TODO("Not yet implemented")
    }
}