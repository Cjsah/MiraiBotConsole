package net.cjsah.console.command.source

import net.cjsah.console.Console
import net.cjsah.console.Permissions
import net.cjsah.console.Util.canUse
import net.cjsah.console.Util.hasPermission
import net.cjsah.console.plugin.Plugin
import net.mamoe.mirai.contact.User
import org.apache.logging.log4j.Level

class UserCommandSource(user: User) : CommandSource<User>(user) {
    override fun hasPermission(permission: Permissions.PermissionType): Boolean {
        return hasPermission(getSource(), permission)
    }

    override fun canUse(plugin: Plugin) = canUse(plugin, getSource().id, true)

    override suspend fun sendFeedBack(message: String) {
        getSource().sendMessage(message)
    }

    override suspend fun sendFeedBack(message: String, level: Level) {
        Console.logger.warn("好友消息不应该以日志形式发送")
        getSource().sendMessage(message)
    }
}