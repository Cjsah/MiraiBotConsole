@file:Suppress("unused")

package net.cjsah.console.command.source

import kotlinx.coroutines.runBlocking
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

    override fun sendFeedBack(message: String) {
        runBlocking { getSource().sendMessage(message) }
    }

    override fun sendFeedBack(message: String, level: Level) {
        Console.logger.warn("群组消息不应该使用日志形式发送")
        runBlocking { getSource().sendMessage(message) }
    }
}