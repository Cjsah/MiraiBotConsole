package net.cjsah.console.plugin

import com.google.gson.JsonObject
import net.cjsah.console.Console
import net.cjsah.console.ConsoleFiles
import net.cjsah.console.Logger
import net.cjsah.console.Util
import net.cjsah.console.command.CommandManager
import net.cjsah.console.exceptions.BuiltExceptions.PLUGIN_UNKNOWN_EXCEPTION
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.exceptions.PluginException
import net.cjsah.console.text.TranslateText
import java.io.File
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

object PluginManager {

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
        Logger.info(
            if (jarFiles.isEmpty()) TranslateText("plugin.load.none")
            else TranslateText("plugin.load.count", jarFiles.size)
        )
        jarFiles.forEach { file ->
            getPlugin(file).let {
                Console.plugins[it.getInfo().id] = it
                Console.permissions.addPlugin(it)
                Logger.info(TranslateText("plugin.loaded", it.getInfo().name, it.getInfo().version))
            }
        }
    }

    fun getPlugin(id: String): Plugin? = Console.plugins[id]

    private fun getPluginJars(): Collection<File> =
        ConsoleFiles.PLUGINS.file.listFiles()?.filter { it.isFile && it.extension == "jar" } ?: emptyList()

    @Throws(Exception::class)
    private fun getPlugin(file: File): Plugin {
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()))
        val inputStream = JarFile(file).let { it.getInputStream(it.getJarEntry("plugin.json")) }
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val info = PluginInformation(Util.GSON.fromJson(reader, JsonObject::class.java))
        inputStream.close()
        reader.close()
        if (Console.plugins.containsKey(info.id))
            throw ConsoleException.create(
                TranslateText("plugin.load.failed", file, info.name, info.id, info.version,
                    TranslateText("plugin.same")
                ), PluginException::class.java
            )
        if (!info.isAvailableVersion())
            throw ConsoleException.create(
                TranslateText("plugin.load.failed", file, info.name, info.id, info.version,
                    TranslateText("plugin.version")
                ), PluginException::class.java
            )
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