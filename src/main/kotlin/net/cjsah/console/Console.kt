@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.console

import kotlinx.coroutines.runBlocking
import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.plugin.Plugin
import net.cjsah.console.plugin.PluginManager
import net.cjsah.console.text.TranslateText
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import java.util.jar.Manifest
import kotlin.concurrent.thread

object Console {
    @JvmField val version: String = Console.javaClass.classLoader.getResource("META-INF/MANIFEST.MF")!!
        .openStream().use { Manifest(it) }.mainAttributes.getValue("Implementation-Version")
    @JvmField val permissions: Permissions = Permissions()
    @JvmField val plugins: MutableMap<String, Plugin> = HashMap()
    private lateinit var bot: Bot
    private var exit = false
    private var freezed = false

    @JvmStatic
    internal suspend fun start(id: Long, password: String, login: Boolean = true) {
        Logger.info(TranslateText("plugin.loading"))
        PluginManager.loadPlugins()

        if (login) Logger.info(TranslateText("bot.login", id))

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
            if (bot.isOnline) Logger.info(TranslateText("bot.success"))
            else throw ConsoleException.create(TranslateText("login.failed"), RuntimeException::class.java)
        }

        ConsoleEvents.register()

        Logger.info(TranslateText("plugin.starting"))
        PluginManager.onBotStarted()

        thread(name = "指令进程") {
            runBlocking {
                while (!exit) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource()) }
                Logger.info(TranslateText("command.stop"))
            }
        }

        Logger.info(TranslateText("console.started"))
    }

    @JvmStatic
    fun stop() {
        this.exit = true

        if (plugins.isNotEmpty()) {
            Logger.info(TranslateText("plugin.stopping"))
            PluginManager.onBotStopped()
        }

        Logger.info(TranslateText("bot.logout"))
        bot.close()

        if (plugins.isNotEmpty()) {
            Logger.info(TranslateText("plugin.removing"))
            PluginManager.onPluginUnload()
        }

        Logger.info(TranslateText("console.exit"))
    }

    @JvmStatic
    fun getBot(): Bot {
        return bot
    }

    @JvmStatic
    fun freeze() {
        if (freezed) throw ConsoleException.create(TranslateText("console.freezed"), RuntimeException::class.java)
        freezed = true
    }

    @JvmStatic
    fun isFrozen(): Boolean {
        return freezed
    }

}
