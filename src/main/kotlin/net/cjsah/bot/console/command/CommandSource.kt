package net.cjsah.bot.console.command

import kotlinx.coroutines.runBlocking
import net.cjsah.bot.console.Console
import net.cjsah.bot.console.Permission
import net.cjsah.bot.console.Util
import net.cjsah.bot.console.command.exceptions.CommandException
import net.cjsah.bot.console.command.exceptions.Para0CommandException
import net.mamoe.mirai.contact.User
import org.hydev.logger.LogLevel


class CommandSource(
    val source: SourceType,
    private val user: User?,
) {
    fun hasPermission(permission: Permission) = permission.level >= user?.let { Util.getPermission(it).level } ?: 0

    @Throws(CommandException::class)
    fun getUserOrException() = user ?: throw Para0CommandException("User not exits or this is console source").create()

    fun sendFeedBack(message: String, consoleMessageLevel: LogLevel = LogLevel.LOG) {
        if (source == SourceType.CONSOLE) {
            when(consoleMessageLevel) {
                LogLevel.LOG -> Console.logger.log(message)
                LogLevel.WARNING -> Console.logger.warning(message)
                LogLevel.ERROR -> Console.logger.error(message)
                LogLevel.DEBUG -> Console.logger.debug(message)
            }
        }else runBlocking {
            user!!.sendMessage(message)
        }
    }
}