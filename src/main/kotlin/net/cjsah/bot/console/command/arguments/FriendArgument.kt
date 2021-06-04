package net.cjsah.bot.console.command.arguments

import net.cjsah.bot.console.Console
import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.arguments.base.Argument
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException
import net.mamoe.mirai.contact.Friend

class FriendArgument private constructor() : Argument<Friend>{

    companion object {
        fun friend() = FriendArgument()

        fun getFriend(context: CommandContext, name: String) = context.getArgument(name, Friend::class.java)
    }

    override fun parse(reader: StringReader): Friend {
        val start = reader.getCursor()
        val number = reader.readLong()
        val friend = Console.bot.getFriend(number)
        if (friend == null) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.friendNotFound().createWithContext(reader, number)
        }
        return friend
    }
}
