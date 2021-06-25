package net.cjsah.bot.console.command.source

import kotlinx.coroutines.runBlocking
import net.cjsah.bot.console.Permission
import net.cjsah.bot.console.util.Util
import net.mamoe.mirai.contact.User
import org.hydev.logger.LogLevel

class UserCommandSource(user: User) : CommandSource<User>(user) {

    override fun hasPermission(permission: Permission) = permission.level >= Util.getPermission(source).level

    override fun sendFeedBack(message: String, consoleMessageLevel: LogLevel) {
        runBlocking { source.sendMessage(message) }
    }
}