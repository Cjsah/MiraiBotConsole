package net.cjsah.bot.console.command

import net.mamoe.mirai.contact.User


class CommandSource(
    val source: SourceType,
    val sourceMember: User?
) {
    val commands = HashMap<String, String>()
}