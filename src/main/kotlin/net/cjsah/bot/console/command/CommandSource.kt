package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.argument.RequiredArgumentType
import net.mamoe.mirai.contact.User
import javax.annotation.Nullable

class CommandSource(
    val source: SourceType,
    val sourceMember: User?
) {
    val commands = HashMap<String, String>()
}