@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.console

import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.plugin.PluginLoader
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.concurrent.thread

object Console {
    private lateinit var bot: Bot
    val logger: Logger = LogManager.getLogger("控制台")
    lateinit var permissions: JsonObject
    private var exit = false

    internal fun start(id: Long, password: String, login: Boolean = true) {
        logger.info("正在加载插件...")
        PluginLoader.loadPlugins()

        if (login) logger.info("登录账号: $id")

        bot = BotFactory.newBot(id, password) {
            fileBasedDeviceInfo("$id.json")
            noBotLog()
            noNetworkLog()
            enableContactCache()
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
            loginSolver = Solver()
        }

        runBlocking {
            if (login) bot.alsoLogin()
        }

        if (login) {
            if (bot.isOnline) logger.info("登录成功")
            else throw RuntimeException("登陆失败")
        }

        ConsoleEvents.register(bot)

        logger.info("正在启动所有插件...")
        PluginLoader.onBotStarted()

        thread(name = "指令进程") {
            while (!exit) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource(Console)) }
            logger.info("指令进程已结束")
        }

        logger.info("控制台已启动")

    }

    internal fun stop() {
        this.exit = true

        logger.info("正在关闭所有插件...")
        PluginLoader.onBotStopped()

        bot.close()

        logger.info("正在卸载所有插件...")
        PluginLoader.onPluginUnload()

        logger.info("控制台退出...")
    }

    fun getBot(): Bot {
        return bot
    }

}
