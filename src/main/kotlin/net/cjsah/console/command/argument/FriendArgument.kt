@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.Console.getBot
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions
import net.mamoe.mirai.contact.Friend

class FriendArgument private constructor() : Argument<Friend> {

    companion object {
        @JvmStatic
        fun friend() = FriendArgument()

        @JvmStatic
        fun getFriend(context: CommandContext, name: String) = context.getArgument(name, Friend::class.java)
    }

    override fun parse(reader: StringReader): Friend {
        val start = reader.getCursor()
        val id = reader.readLong()
        val friend = getBot().getFriend(id)
        if (friend == null) {
            reader.setCursor(start)
            throw BuiltExceptions.FRIEND_NOT_FOUND.createWithContext(reader, id)
        }
        return friend
    }
}