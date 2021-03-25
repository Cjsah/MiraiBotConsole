package net.cjsah.bot.console

object ConsoleCommand {
    private val commands = initCommand()

    private fun initCommand(): HashMap<String, Command> {
        val map = HashMap<String, Command>()
        map["stop"] = Command { Console.stopConsole = true }
        map["reload"] = Command { Console.reloadAllPlugins() }
        return map
    }

    internal fun run(command: String?) {
        command?.let {
            commands.getOrDefault(it, null)?.run()
        }
    }

    @Suppress("unused")
    fun registerCommand(name: String, command: Command) {
        commands[name] = command
    }
}