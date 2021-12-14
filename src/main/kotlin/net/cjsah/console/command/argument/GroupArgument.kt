@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.Console.getBot
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions
import net.mamoe.mirai.contact.Group

class GroupArgument private constructor() : Argument<Group> {

    companion object {
        @JvmStatic
        fun group() = GroupArgument()

        @JvmStatic
        fun getGroup(context: CommandContext, name: String) = context.getArgument(name, Group::class.java)
    }

    override fun parse(reader: StringReader): Group {
        val start = reader.getCursor()
        val id = reader.readLong()
        val group = getBot().getGroup(id)
        if (group == null) {
            reader.setCursor(start)
            throw BuiltExceptions.GROUP_NOT_FOUND.createWithContext(reader, id)
        }
        return group
    }
}