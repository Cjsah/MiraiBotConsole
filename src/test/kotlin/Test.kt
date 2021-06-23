
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.bot.console.Console
import net.cjsah.bot.console.ConsoleEvents
import net.cjsah.bot.console.Files
import net.cjsah.bot.console.LogAppender
import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.source.ConsoleCommandSource
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.ANDROID_PAD
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility


suspend fun main() {
//    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    if (System.getProperty("nocolor") != null) HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED
    val logger = Console.logger
    val config = net.cjsah.bot.console.AccountConfig()
    if (net.cjsah.bot.console.initFiles(config)) {
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

    net.cjsah.bot.console.startListener()

    logger.log("控制台退出...")
}

private fun startListener() {
    Console.logger.log("控制台已启动")
    // 控制台命令
    while (!Console.stopConsole) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource(Console)) }

    Console.unloadAllPlugins()

    Console.bot.close()
}
