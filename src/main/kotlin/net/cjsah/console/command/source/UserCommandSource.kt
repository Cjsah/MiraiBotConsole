package net.cjsah.console.command.source

import kotlinx.coroutines.runBlocking
import net.cjsah.console.Permission
import net.cjsah.console.util.Util
import net.mamoe.mirai.contact.User
import org.hydev.logger.LogLevel

class UserCommandSource(user: User) : CommandSource<User>(user) {

    override fun hasPermission(permission: Permission) = permission.level >= Util.getPermission(source).level

    override fun sendFeedBack(message: String, consoleMessageLevel: LogLevel) {
        runBlocking { source.sendMessage(message) }
    }
}