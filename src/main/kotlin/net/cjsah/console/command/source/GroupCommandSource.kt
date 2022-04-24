@file:Suppress("unused")

package net.cjsah.console.command.source

import net.cjsah.console.Console
import net.cjsah.console.Permissions
import net.cjsah.console.Util.canUse
import net.cjsah.console.Util.hasPermission
import net.cjsah.console.plugin.Plugin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import org.apache.logging.log4j.Level

class GroupCommandSource(group: Group, private val sender: Member) : CommandSource<Group>(group) {
    override fun hasPermission(permission: Permissions.PermissionType) = hasPermission(sender, permission)

    override fun canUse(plugin: Plugin) = canUse(plugin, getSource().id, false)

    fun memberCanUse(plugin: Plugin) = canUse(plugin, sender.id, true)

    override suspend fun sendFeedBack(message: String) {
        getSource().sendMessage(message)
    }

    override suspend fun sendFeedBack(message: String, level: Level) {
        Console.logger.warn("群组消息不应该以日志形式发送")
        getSource().sendMessage(message)
    }
}