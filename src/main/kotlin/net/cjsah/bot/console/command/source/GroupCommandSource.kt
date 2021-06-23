package net.cjsah.bot.console.command.source

import kotlinx.coroutines.runBlocking
import net.cjsah.bot.console.Permission
import net.cjsah.bot.console.Util
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import org.hydev.logger.LogLevel

@Suppress("MemberVisibilityCanBePrivate")
class GroupCommandSource(group: Group, val sender: Member) : CommandSource<Group>(group) {

    override fun hasPermission(permission: Permission) = permission.level <= Util.getPermission(sender).level

    override fun sendFeedBack(message: String, consoleMessageLevel: LogLevel) {
        runBlocking {
            source.sendMessage(message)
        }
    }
}