package net.cjsah.console.events

import net.cjsah.console.command.CommandManager

object CommandRegistration {
    val EVENT get() = CommandManager.dispatcher
}