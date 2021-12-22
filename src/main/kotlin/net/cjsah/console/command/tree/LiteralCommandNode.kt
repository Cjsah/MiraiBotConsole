package net.cjsah.console.command.tree

import net.cjsah.console.command.Command
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.command.context.Range
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
    override fun parse(reader: StringReader, builder: ContextBuilder) {
        val start = reader.getCursor()
        val end = parse(reader)
        if (end < 0) throw BuiltExceptions.LITERAL_INCORRECT.createWithContext(reader, literal)
        builder.withRange(Range(start, end))
    }

    override fun createBuilder() =  LiteralArgumentBuilder.literal(literal).apply {
        this.requires(super.getRequirement())
        if (super.getCommand() != null) this.executes(super.getHelp(), super.getCommand())
    }

    override fun getSortedKey() = literal

    private fun parse(reader: StringReader): Int {
        if (reader.canRead(literal.length)) {
            val start = reader.getCursor()
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

}