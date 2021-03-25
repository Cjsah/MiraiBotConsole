package net.cjsah.bot.console

import net.mamoe.mirai.Bot
import org.hydev.logger.HyLogger
import org.hydev.logger.foreground
import java.awt.Color
import java.io.File

@Suppress("unused")
abstract class Plugin(
    /**
     * 插件名
     */
    val pluginName: String,
    /**
     * 插件版本
     */
    val pluginVersion: String,
    /**
     * 是否有配置文件
     */
    val hasConfig: Boolean,
    /**
     * 插件作者
     */
    val pluginAuthors: List<String>
) {
    lateinit var bot: Bot

    val logger = HyLogger("${Color(204, 255, 51).foreground()}$pluginName")

    val pluginDir: File
        get() = File(Files.PLUGINS.file, pluginName)

    open suspend fun onPluginStart() {
    }

    open suspend fun onPluginStop() {

    }

}