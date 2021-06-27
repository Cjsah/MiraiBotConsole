@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.bot.console

import com.google.common.collect.Lists
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.source.ConsoleCommandSource
import net.cjsah.bot.console.plugin.ConsolePlugin
import net.cjsah.bot.console.plugin.Plugin
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.hydev.logger.HyLogger
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.concurrent.thread

object Console {
    private lateinit var bot: Bot
    val logger = HyLogger("控制台")
    lateinit var permissions: JsonObject
    private lateinit var listener: Thread
    private val loadedPlugins = mutableListOf<Plugin>()
    private var exit = false

    internal fun start(id: Long, password: String, login: Boolean = true) {
        if (login) logger.log("登录账号: $id")

        bot = BotFactory.newBot(id, password) {
            fileBasedDeviceInfo("$id.json")
            noBotLog()
            noNetworkLog()
            enableContactCache()
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
        }

        runBlocking {
            if (login) bot.alsoLogin()
        }

        if (login) {
            if (bot.isOnline) logger.log("登录成功")
            else throw RuntimeException("登陆失败")
        }
        ConsoleEvents.register(bot)
        logger.log("正在加载插件...")
        loadAllPlugins()
        logger.log("插件加载完成")

        listener = thread(name = "控制台监控") {
            while (!exit) readLine()?.let { if (it != "") CommandManager.execute(it, ConsoleCommandSource(Console)) }
        }

        logger.log("控制台已启动")

    }

    fun stop() {
        this.exit = true

        unloadAllPlugins()

        bot.close()

        logger.log("控制台退出...")
    }

    fun getBot(): Bot {
        return bot
    }

    fun loadPlugin(plugin: Plugin, log: Boolean = true) = runBlocking {
        withContext(Dispatchers.IO) {
            plugin.bot = bot
            if (plugin.hasConfig && !plugin.pluginDir.exists()) plugin.pluginDir.mkdir()
            plugin.onPluginStart()
            loadedPlugins.add(plugin)
            if (log) logger.log("${plugin.pluginName} ${plugin.pluginVersion} 插件已启动!")
        }
    }

    fun unloadPlugin(plugin: Plugin) = runBlocking {
        withContext(Dispatchers.IO) {
            plugin.onPluginStop()
            logger.log("${plugin.pluginName} 插件已关闭!")
        }
    }

    private fun loadAllPlugins() {
        loadPlugin(ConsolePlugin.get(), false)
        getPluginJars().forEach { pluginFile -> getPlugin(pluginFile)?.let { plugin -> loadPlugin(plugin) } }
    }

    private fun unloadAllPlugins() {
        loadedPlugins.removeIf { plugin ->
            if (plugin is ConsolePlugin) {
                false
            }else {
                unloadPlugin(plugin)
                true
            }
        }
    }

    private fun getPluginJars(): List<File> {
        val jars = Lists.newArrayList<File>()
        Files.PLUGINS.file.listFiles()?.forEach {
            if (it.isFile && it.name.endsWith(".jar")) jars.add(it)
        }
        return jars
    }

    private fun getPlugin(jar: File): Plugin? {
        return try {
            val ucl = URLClassLoader(arrayOf(URL("jar:${jar.toURI().toURL()}!/")))

            Class.forName(JarFile(jar).manifest.mainAttributes.getValue("Plugin-Class")!!, true, ucl)
                .getDeclaredConstructor().newInstance() as Plugin

        }catch (e: Exception) {
            val logger = Console.logger
            when (e) {
                is NullPointerException -> logger.warning("找不到插件 ${jar.name} 启动入口点, 请在插件中设置Plugin-Main启动入口")
                is ClassNotFoundException -> logger.warning("找不到插件 ${jar.name} 启动入口点, 请正确设置插件Plugin-Main启动入口")
                else -> logger.warning("插件 ${jar.name} 加载发生未知错误")
            }
            logger.error("插件 ${jar.name} 加载失败")
            null
        }
    }
}
