package net.cjsah.bot.console

import net.cjsah.bot.console.Console.stopConsole
import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.SourceType

class ConsolePlugin: Plugin<ConsolePlugin>(
    "ConsolePlugin",
    "0.0.1",
    false,
    listOf("Cjsah")
) {

    companion object {
        internal lateinit var thisPlugin: ConsolePlugin
    }

    override suspend fun onPluginStart() {
        thisPlugin = this
        commandRegister(SourceType.CONSOLE, CommandManager.literal("stop").executes { stopConsole = true })
    }
}