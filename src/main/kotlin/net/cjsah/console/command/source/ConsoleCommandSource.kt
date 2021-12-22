package net.cjsah.console.command.source

import net.cjsah.console.Console
import net.cjsah.console.Console.logger
import net.cjsah.console.Permissions
import net.cjsah.console.plugin.Plugin
import org.apache.logging.log4j.Level

class ConsoleCommandSource : CommandSource<Console>(Console) {
    override fun hasPermission(permission: Permissions.PermissionType) = true

    override fun canUse(plugin: Plugin) = true

    override fun sendFeedBack(message: String) = sendFeedBack(message, Level.INFO)

    override fun sendFeedBack(message: String, level: Level) {
        when (level) {
            Level.INFO -> logger.info(message)
            Level.WARN -> logger.warn(message)
            Level.ERROR -> logger.error(message)
            Level.DEBUG -> logger.debug(message)
        }
    }
}