package net.cjsah.bot.console

import com.google.common.collect.Lists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import org.hydev.logger.HyLogger
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

@Suppress("MemberVisibilityCanBePrivate")
object Console {
    lateinit var bot: Bot
    var stopConsole = false
    val logger = HyLogger("控制台")
    private val loadedPlugins = mutableListOf<Plugin<*>>()

    fun loadPlugin(plugin: Plugin<*>, log: Boolean = true) = runBlocking {
        withContext(Dispatchers.IO) {
            plugin.bot = bot
            if (plugin.hasConfig && !plugin.pluginDir.exists()) plugin.pluginDir.mkdir()
            plugin.onPluginStart()
            loadedPlugins.add(plugin)
            if (log) logger.log("${plugin.pluginName} ${plugin.pluginVersion} 插件已启动!")
        }
    }

    fun unloadPlugin(plugin: Plugin<*>) = runBlocking {
        withContext(Dispatchers.IO) {
            plugin.onPluginStop()
            logger.log("${plugin.pluginName} 插件已关闭!")
        }
    }

    fun loadAllPlugins() {
        loadPlugin(ConsolePlugin(), false)
        getPluginJars().forEach { pluginFile -> getPlugin(pluginFile)?.let { plugin -> loadPlugin(plugin) } }
    }

    fun unloadAllPlugins() {
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

    private fun getPlugin(jar: File): Plugin<*>? {
        return try {
            val ucl = URLClassLoader(arrayOf(URL("jar:${jar.toURI().toURL()}!/")))

            Class.forName(JarFile(jar).manifest.mainAttributes.getValue("Plugin-Class")!!, true, ucl)
                .getDeclaredConstructor().newInstance() as Plugin<*>

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