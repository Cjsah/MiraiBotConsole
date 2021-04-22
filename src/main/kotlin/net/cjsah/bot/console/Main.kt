package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.ANDROID_PAD
import java.io.File

// ***本程序部分代码借鉴于小神***

internal class AccountConfig : HyConfig(Files.ACCOUNT.file, false, true) {
    override fun save(): Boolean {
        return try {
            save(this.configFile)
            true
        } catch (e: Exception) {
            false
        }
    }
}

internal enum class Files(file: String, val isDirectory: Boolean) {
    ACCOUNT("account.yml", false),
    PLUGINS("plugins", true),
    IMAGES("images", true);

    val file: File = File(file)
}

suspend fun main() {
    System.setProperty("mirai.no-desktop", "")
    val logger = Console.logger
    val config = AccountConfig()
    if (initFiles(config)) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    config.load()
    logger.log("登录账号: ${config.getLong("account")}")

    Console.bot = BotFactory.newBot(config.getLong("account"), config.getString("password")) {
        fileBasedDeviceInfo("${config.getLong("account")}.json")
        noBotLog()
        noNetworkLog()
        enableContactCache()
        protocol = ANDROID_PAD
    }.alsoLogin()

    if (Console.bot.isOnline) logger.log("登录成功")
    ConsoleEvents.registerEvents(Console.bot)
    logger.log("控制台初始化完成")
    logger.log("正在加载插件...")
    Console.loadAllPlugins()
    logger.log("插件加载完成")

    startListener()

    logger.log("控制台退出...")
}

private fun startListener() {
    Console.logger.log("控制台启动完成")
    while (!Console.stopConsole) ConsoleCommand.run(readLine())

    Console.unloadAllPlugins()

    Console.bot.close()
}

private fun initFiles(config: AccountConfig): Boolean {
    var init = false
    Files.values().forEach {
        if (it == Files.ACCOUNT && !it.file.exists()) {
            with(config) {
                it.file.createNewFile()
                set("account", "QQ")
                set("password", "密码")
                save()
                init = true
            }
        }
        if (!it.file.exists()) {
            if (it.isDirectory) it.file.mkdirs()
            else it.file.createNewFile()
        }
    }
    return init
}
