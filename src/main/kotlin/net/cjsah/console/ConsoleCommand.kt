package net.cjsah.console

object ConsoleCommand {
    private val commands = initCommand()

    private fun initCommand(): Map<String, Command> {
        val map = HashMap<String, Command>()
        map["stop"] = Command { Console.stopConsole = true }
        map["reload"] = Command { Console.reloadAllPlugins() }
        return map
    }

    fun run(command: String?) {
        command?.let {
            this.commands.getOrDefault(it, null)?.run()
        }
    }
}