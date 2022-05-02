package net.cjsah.console.command.source

import net.cjsah.console.Console
import net.cjsah.console.Logger
import net.cjsah.console.Permissions
import net.cjsah.console.plugin.Plugin
import org.apache.logging.log4j.Level

class ConsoleCommandSource : CommandSource<Console>(Console) {
    override fun hasPermission(permission: Permissions.PermissionType) = true

    override fun canUse(plugin: Plugin) = true

    override suspend fun sendFeedBack(message: String) = sendFeedBack(message, Level.INFO)

    override suspend fun sendFeedBack(message: String, level: Level) {
        when (level) {
            Level.INFO -> Logger.info(message)
            Level.WARN -> Logger.warn(message)
            Level.ERROR -> Logger.error(message)
            Level.DEBUG -> Logger.debug(message)
        }
    }
}