package net.cjsah.bot.console

import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.SourceType
import net.cjsah.bot.console.events.CommandRegistration

class ConsolePlugin private constructor(): Plugin(
    "ConsolePlugin",
    "0.0.1",
    false,
    listOf("Cjsah")
) {

    companion object {
        private val plugin = ConsolePlugin()
        fun get() = plugin
    }

    override suspend fun onPluginStart() {
        CommandRegistration.EVENT.register(SourceType.CONSOLE, CommandManager.literal("stop").executes {
            Console.stopConsole = true
        })
    }
}