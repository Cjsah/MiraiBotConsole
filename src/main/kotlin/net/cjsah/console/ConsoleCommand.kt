package net.cjsah.console

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ConsoleCommand {
    private val commands = initCommand()

    private fun initCommand(): Map<String, Command> {
        val map = HashMap<String, Command>()
        map["stop"] = object : Command {
            override suspend fun command() {
                Console.stopConsole = true
            }
        }
        GlobalScope.launch {

        }
        map["reload"] = object : Command {
            override suspend fun command() {
                Console.reloadAllPlugins()
            }
        }
        return map
    }

    fun run(command: String?) {
        command?.let {
            if (this.commands.containsKey(it)) {
                this.commands[it]!!.run()
            }
        }
    }
}