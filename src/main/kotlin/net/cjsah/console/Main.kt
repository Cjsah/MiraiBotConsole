package net.cjsah.console

fun main() {
    System.setProperty("mirai.no-desktop", "")

    val logger = Console.logger
    val config = Account.get()

    if (ConsoleFiles.init()) {
        logger.info("初始化完成")
        logger.info("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    Console.permissions = Util.fromJson(ConsoleFiles.PERMISSIONS.file)

    ConsoleCommand.register()

    Console.start(config.getLong("account"), config.getString("password"))

}

//fun isDevelopment(): Boolean {
//    return System.getProperty("development") != null
//}
//
