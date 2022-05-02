@file:Suppress("unused")

package net.cjsah.console.command.source

import net.cjsah.console.Console
import net.cjsah.console.Logger
import net.cjsah.console.Permissions
import net.cjsah.console.Util.canUse
import net.cjsah.console.Util.hasPermission
import net.cjsah.console.plugin.Plugin
import net.cjsah.console.text.TranslateText
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
        Logger.warn(TranslateText("message.logger.group"))
        getSource().sendMessage(message)
    }
}