import net.cjsah.bot.console.*
import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.SourceType
import net.cjsah.bot.console.command.exceptions.Para0CommandException
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility

fun main() {
    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    if (System.getProperty("nocolor") != null) HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED
    val logger = Console.logger

    Console.bot = BotFactory.newBot(123456, "123456") {
        fileBasedDeviceInfo("${123456}.json")
        noBotLog()
        noNetworkLog()
        enableContactCache()
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
    }

    ConsoleEvents.registerEvents(Console.bot)
    logger.log("正在加载插件...")
    Console.loadAllPlugins()
    logger.log("插件加载完成")

    startListener()

    logger.log("控制台退出...")
}
private fun startListener() {
    // 控制台命令
    while (!Console.stopConsole) readLine()?.let { if (it != "") CommandManager.execute(it, CommandSource(SourceType.CONSOLE, null)) }

    Console.unloadAllPlugins()
}
