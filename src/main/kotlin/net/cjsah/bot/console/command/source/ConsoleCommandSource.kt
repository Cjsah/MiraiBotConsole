package net.cjsah.bot.console.command.source

import net.cjsah.bot.console.Console
import net.cjsah.bot.console.Permission
import org.hydev.logger.LogLevel

class ConsoleCommandSource(console: Console) : CommandSource<Console>(console) {
    override fun hasPermission(permission: Permission) = true

    override fun sendFeedBack(message: String, consoleMessageLevel: LogLevel) {
        when(consoleMessageLevel) {
            LogLevel.LOG -> Console.logger.log(message)
            LogLevel.WARNING -> Console.logger.warning(message)
            LogLevel.ERROR -> Console.logger.error(message)
            LogLevel.DEBUG -> Console.logger.debug(message)
        }
    }
}