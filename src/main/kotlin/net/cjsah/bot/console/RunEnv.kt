package net.cjsah.bot.console


suspend fun main(args: Array<String>) {
    Console.addConsolePlugin(TestPlugin())
    main()
}

class TestPlugin : Plugin(
    "TestPlugin",
    "x.x.x",
    true,
    listOf("Cjsah")
) {
    override suspend fun onPluginStart() {
        logger.log("test")
    }

    override suspend fun onPluginStop() { }
}