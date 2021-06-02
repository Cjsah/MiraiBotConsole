import net.cjsah.bot.console.*
import net.cjsah.bot.console.command.SourceType
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility

fun main() {
    System.setProperty("mirai.no-desktop", "")
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
    while (!Console.stopConsole) readLine()?.let { Commands.runCommand(it, SourceType.CONSOLE, null) }

    Console.unloadAllPlugins()
}
