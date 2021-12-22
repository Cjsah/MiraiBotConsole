package net.cjsah.console.plugin

import com.google.gson.JsonObject
import net.cjsah.console.Console
import net.cjsah.console.Console.logger
import net.cjsah.console.Console.permissions
import net.cjsah.console.ConsoleFiles
import net.cjsah.console.Util
import net.cjsah.console.exceptions.PluginException
import java.io.File
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

object PluginLoader {

    fun onBotStarted() = Console.plugins.values.forEach { it.onBotStarted() }


    fun onBotStopped() = Console.plugins.values.forEach { it.onBotStopped() }

    fun onPluginUnload() = Console.plugins.entries.removeAll { (_, plugin) ->
        plugin.onPluginUnload()
        true
    }

    @Throws(Exception::class)
    fun loadPlugins() {
        val jars = getPluginJars()
        logger.info(if (jars.isEmpty()) "没有插件被加载" else "正在加载 ${jars.size} 个插件")
        for (jar in jars) {
            val plugin = getPlugin(jar)
            Console.plugins[plugin.getInfo().id] = plugin
            permissions.addPlugin(plugin)
            logger.info("插件 ${plugin.getInfo().name} ${plugin.getInfo().version} 已加载")
        }
    }

    fun getPlugin(id: String): Plugin? {
        return Console.plugins[id]
    }

    private fun getPluginJars(): Collection<File> {
        val files = ConsoleFiles.PLUGINS.file.listFiles() ?: return emptyList()
        return files.filter { it.isFile && it.name.endsWith(".jar") && !it.name.startsWith(".") }
    }

    @Throws(Exception::class)
    private fun getPlugin(file: File): Plugin {
        val jarResource = javaClass.classLoader.getResource(file.name)
        val classLoader = URLClassLoader(arrayOf(jarResource))
        val jarFile = JarFile(file)
        val infoEntry = jarFile.getJarEntry("plugin.json")
        val inputStream = jarFile.getInputStream(infoEntry)
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val json: JsonObject = Util.GSON.fromJson(reader, JsonObject::class.java)
        inputStream.close()
        reader.close()
        val info = PluginInformation(json)
        if (Console.plugins.containsKey(info.id))
            throw PluginException("插件 $file [${info.name} (${info.id}) v${info.version}] 无法加载, 已有同名插件 ${Console.plugins[info.id]}")
        val clazz = classLoader.loadClass(info.main)
        val plugin = clazz.getDeclaredConstructor().newInstance() as Plugin
        plugin.init(info)
        return plugin
    }

}