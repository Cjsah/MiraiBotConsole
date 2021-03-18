package net.cjsah.console

import com.google.common.collect.Lists
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import org.hydev.logger.HyLogger
import java.io.File

object Console {
    lateinit var bot: Bot
    val logger = HyLogger("控制台")
    private val loadingPlugins = mutableListOf<Plugin>()

    private suspend fun loadPlugin(plugin: Plugin) {
        plugin.bot = this.bot
        if (plugin.hasConfig && !plugin.pluginDir.exists()) plugin.pluginDir.mkdir()
        plugin.onPluginStart()
        this.loadingPlugins.add(plugin)
        plugin.logger.log("插件已启动!")
    }

    private suspend fun unloadPlugin(plugin: Plugin) {
        plugin.onPluginStop()
        loadingPlugins.remove(plugin)
        plugin.logger.log("插件已关闭!")
    }

    suspend fun reloadPlugin(plugin: Plugin) {
        unloadPlugin(plugin)
        loadPlugin(plugin)
    }

    suspend fun reloadAllPlugins() {
        unloadAllPlugins()
        loadAllPlugins()
    }

    suspend fun loadAllPlugins() {
        getPluginJars().forEach {
            loadPlugin(getPlugin(it))
        }
    }


    suspend fun unloadAllPlugins() {
        loadingPlugins.forEach {
            unloadPlugin(it)
        }
    }

}


fun main() {
    val logger = Console.logger
    val config = AccountConfig()
    if (initFiles(config)) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    config.load()
    logger.log("登录账号: ${config.getLong("account")}")

    Console.bot = BotFactory.newBot(config.getLong("account"), config.getString("password"))
}

fun initFiles(config: AccountConfig): Boolean {
    var init = false
    Files.values().forEach {
        if (!it.file.exists()) {
            if (it.isDirectory) it.file.mkdirs()
            else it.file.createNewFile()
        }
        if (it == Files.ACCOUNT) {
            with(config) {
                set("account", "QQ")
                set("password", "密码")
                save()
                init = true
            }
        }
    }
    return init
}

fun getPluginJars(): List<File> {
    val jars = Lists.newArrayList<File>()
    Files.PLUGINS.file.listFiles()?.forEach {
        if (it.isFile && it.name.endsWith(".jar")) jars.add(it)
    }
    return jars
}

fun getPlugin(jar: File): Plugin {

}