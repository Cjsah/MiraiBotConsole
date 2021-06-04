package net.cjsah.bot.console.events

import net.cjsah.bot.console.command.CommandManager

object CommandRegistration {
    val EVENT get() = CommandManager.dispatcher
}