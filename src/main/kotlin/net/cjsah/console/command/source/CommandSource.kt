package net.cjsah.console.command.source

import net.cjsah.console.Permission
import org.hydev.logger.LogLevel

abstract class CommandSource<T>(
    val source: T
) {
    abstract fun hasPermission(permission: Permission): Boolean

    abstract fun sendFeedBack(message: String, consoleMessageLevel: LogLevel = LogLevel.LOG)
}