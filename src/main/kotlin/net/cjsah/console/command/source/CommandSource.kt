package net.cjsah.console.command.source

import net.cjsah.console.Permissions.PermissionType
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.plugin.Plugin
import org.apache.logging.log4j.Level

abstract class CommandSource<T>(private val source: T) {

    abstract fun hasPermission(permission: PermissionType): Boolean

    abstract fun canUse(plugin: Plugin): Boolean

    @Throws(CommandException::class)
    abstract fun sendFeedBack(message: String)

    @Throws(CommandException::class)
    abstract fun sendFeedBack(message: String, level: Level)

    open fun getSource(): T {
        return source
    }

}