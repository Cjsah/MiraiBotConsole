package net.cjsah.bot.console.command

import kotlinx.coroutines.runBlocking
import net.cjsah.bot.console.Permission
import net.cjsah.bot.console.Plugin
import net.cjsah.bot.console.Util
import net.cjsah.bot.console.command.exceptions.CommandException
import net.cjsah.bot.console.command.exceptions.Para0CommandException
import net.mamoe.mirai.contact.User
import org.hydev.logger.LogLevel


class CommandSource(
    val source: SourceType,
    private val user: User?,
    private val plugin: Plugin
) {
    val commands = HashMap<String, String>()

    fun hasPermission(permission: Permission) = permission.level >= user?.let { Util.getPermission(it).level } ?: 0

    @Throws(CommandException::class)
    fun getUserOrException() = user ?: throw Para0CommandException("User not exits or this is console source").create()

    fun sendFeedBack(message: String, consoleMessageLevel: LogLevel = LogLevel.LOG) {
        if (source == SourceType.CONSOLE) {
            val logger = plugin.logger
            when(consoleMessageLevel) {
                LogLevel.LOG -> logger.log(message)
                LogLevel.WARNING -> logger.warning(message)
                LogLevel.ERROR -> logger.error(message)
                LogLevel.DEBUG -> logger.debug(message)
            }
        }else runBlocking {
            user!!.sendMessage(message)
        }
    }
}