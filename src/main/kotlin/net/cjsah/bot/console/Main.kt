package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.source.ConsoleCommandSource
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.ANDROID_PAD
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility
import java.io.File
import net.cjsah.bot.console.gui.FloatWindow.floatWindow

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
    PERMISSIONS("permissions.json", false),
    ACCOUNT("account.yml", false),
    PLUGINS("plugins", true),
    IMAGES("images", true);

    val file: File = File(file)
}

suspend fun main() {
//    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    if (System.getProperty("nocolor") != null) HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED
    val logger = Console.logger
    val config = AccountConfig()
    if (initFiles(config)) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())

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
    logger.log("正在加载插件...")
    Console.loadAllPlugins()
    logger.log("插件加载完成")

    try{
        floatWindow()
        logger.log("悬浮窗加载成功")
    }catch (e: Exception) {
        logger.log("悬浮窗加载失败")
    }

    startListener()

    logger.log("控制台退出...")
}

private fun startListener() {
    Console.logger.log("控制台已启动")
    // 控制台命令
    while (!Console.stopConsole) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource(Console)) }

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
        }else if (it == Files.PERMISSIONS && !it.file.exists()) {
            val json = JsonObject()
            json.add("owner", JsonArray())
            json.add("admin", JsonArray())
            json.add("helper", JsonArray())
            it.file.createNewFile()
            it.file.writeText(Util.GSON.toJson(json))
            println("ok")
        }else if (!it.file.exists()) {
            if (it.isDirectory) it.file.mkdirs()
            else it.file.createNewFile()
        }
    }
    return init
}
