package net.cjsah.bot.console.command.arguments

import net.cjsah.bot.console.Console
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.arguments.base.Argument
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.exceptions.CommandException
import net.mamoe.mirai.contact.Group

class GroupArgument private constructor(): Argument<Group> {

    companion object {
        fun group() = GroupArgument()

        fun getGroup(context: CommandContext, name: String) = context.getArgument(name, Group::class.java)
    }

    override fun parse(reader: StringReader): Group {
        val start = reader.getCursor()
        val number = reader.readLong()
        val group = Console.getBot().getGroup(number)
        if (group == null) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.groupNotFound().createWithContext(reader, number)
        }
        return group
    }
}