package net.cjsah.bot.console

class ConsolePlugin: Plugin(
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

    }
}