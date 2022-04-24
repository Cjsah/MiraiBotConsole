@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.console

import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.plugin.Plugin
import net.cjsah.console.plugin.PluginLoader
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.jar.Manifest
import kotlin.concurrent.thread

object Console {
    @JvmField val version: String = Console.javaClass.classLoader.getResource("META-INF/MANIFEST.MF")!!
        .openStream().use { Manifest(it) }.mainAttributes.getValue("Implementation-Version")
    @JvmField val logger: Logger = LogManager.getLogger("控制台")
    @JvmField val permissions: Permissions = Permissions()
    @JvmField val plugins: MutableMap<String, Plugin> = HashMap()
    private lateinit var bot: Bot
    private var exit = false
    private var freezed = false

    @JvmStatic
    internal suspend fun start(id: Long, password: String, login: Boolean = true) {
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

        if (login) {
            bot.alsoLogin()
            if (bot.isOnline) logger.info("登录成功")
            else throw ConsoleException("登陆失败")
        }

        ConsoleEvents.register()

        logger.info("正在启动所有插件...")
        PluginLoader.onBotStarted()

        thread(name = "指令进程") {
            while (!exit) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource()) }
            logger.info("指令进程已结束")
        }

        logger.info("控制台已启动")

    }

    @JvmStatic
    fun stop() {
        this.exit = true

        if (plugins.isNotEmpty()) {
            logger.info("正在关闭所有插件...")
            PluginLoader.onBotStopped()
        }

        logger.info("QQ账号退出登陆...")
        bot.close()

        if (plugins.isNotEmpty()) {
            logger.info("正在卸载所有插件...")
            PluginLoader.onPluginUnload()
        }

        logger.info("控制台退出...")
    }

    @JvmStatic
    fun getBot(): Bot {
        return bot
    }

    fun freeze() {
        if (freezed) throw ConsoleException("控制台已冻结")
        freezed = true
    }

    fun isFreezed(): Boolean {
        return freezed
    }

}
