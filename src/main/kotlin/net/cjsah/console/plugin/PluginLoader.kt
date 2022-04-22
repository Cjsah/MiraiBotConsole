package net.cjsah.console.plugin

import com.google.gson.JsonObject
import net.cjsah.console.Console
import net.cjsah.console.Console.logger
import net.cjsah.console.ConsoleFiles
import net.cjsah.console.Util
import net.cjsah.console.command.CommandManager
import net.cjsah.console.exceptions.BuiltExceptions.PLUGIN_UNKNOWN_EXCEPTION
import net.cjsah.console.exceptions.PluginException
import java.io.File
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

object PluginLoader {

    fun onBotStarted() = Console.plugins.values.forEach { it.onBotStarted() }

    fun onBotStopped() = Console.plugins.values.forEach {
        CommandManager.deregister(it)
        it.onBotStopped()
    }

    fun onPluginUnload() = Console.plugins.entries.removeAll { (_, plugin) ->
        plugin.onPluginUnload()
        true
    }

    @Throws(Exception::class)
    fun loadPlugins() {
        val jarFiles = getPluginJars()
        logger.info(if (jarFiles.isEmpty()) "没有插件被加载" else "正在加载 ${jarFiles.size} 个插件")
        jarFiles.forEach { file ->
            getPlugin(file).let {
                Console.plugins[it.getInfo().id] = it
                Console.permissions.addPlugin(it)
                logger.info("插件 ${it.getInfo().name} ${it.getInfo().version} 已加载")
            }
        }
    }

    fun getPlugin(id: String): Plugin? = Console.plugins[id]

    private fun getPluginJars(): Collection<File> =
        ConsoleFiles.PLUGINS.file.listFiles().filter { it.isFile && it.extension == "jar" }

    @Throws(Exception::class)
    private fun getPlugin(file: File): Plugin {
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()))
        val inputStream = JarFile(file).let { it.getInputStream(it.getJarEntry("plugin.json")) }
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val info = PluginInformation(Util.GSON.fromJson(reader, JsonObject::class.java))
        inputStream.close()
        reader.close()
        if (Console.plugins.containsKey(info.id))
            throw PluginException("插件 $file [${info.name} (${info.id}) v${info.version}] 无法加载, 已有同名插件 ${Console.plugins[info.id]}")
        if (!info.isAvailableVersion())
            throw PluginException("插件 $file [${info.name} (${info.id}) v${info.version}] 无法加载, 插件版本与控制台版本不一致")
        return (classLoader.loadClass(info.main).getDeclaredConstructor().newInstance() as Plugin).also { it.init(info) }
    }

    private fun PluginInformation.isAvailableVersion(): Boolean {
        if (this.depends.containsKey("console")) {
            val consoleVersions = Console.version.split(".").map { it.toInt() }
            val pluginVersion = this.depends["console"]!!
            val pluginPrefix = pluginVersion.substring(0, 1)
            val pluginSuffix = pluginVersion.substring(pluginVersion.length - 1, pluginVersion.length)
            val pluginVersions = pluginVersion.substring(1, pluginVersion.length - 1).split(".").map { it.toInt() }
            val prefixRule: (Int, Int) -> Boolean = when (pluginPrefix) {
                "[" -> { a, b -> a >= b }
                "(" -> { a, b -> a > b }
                else -> throw PLUGIN_UNKNOWN_EXCEPTION.create()
            }
            val suffixRule: (Int, Int) -> Boolean = when (pluginSuffix) {
                "]" -> { a, b -> a <= b }
                ")" -> { a, b -> a < b }
                else -> throw PLUGIN_UNKNOWN_EXCEPTION.create()
            }
            for (index in pluginVersions.indices) {
                if (!(prefixRule(pluginVersions[index], consoleVersions[index]) &&
                      suffixRule(pluginVersions[index], consoleVersions[index])))
                    return false
            }
            return true
        } else return true
    }
}