package net.cjsah.console

suspend fun main() {
    System.setProperty("mirai.no-desktop", "")

    Language.load()

    val config = Account.get()

    if (ConsoleFiles.init()) {
        Logger.info("初始化完成")
        Logger.info("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    Console.permissions.load()
    ConsoleCommand.register()
    Console.freeze()


    Console.start(config.getLong("account"), config.getString("password"), false)

}

