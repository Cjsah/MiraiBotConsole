package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.mamoe.mirai.Bot
import org.hydev.logger.HyLogger
import org.hydev.logger.foreground
import java.awt.Color
import java.io.File
import java.util.function.Consumer

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
    /**
     * Bot
     */
    lateinit var bot: Bot

    /**
     * logger日志输出
     */
    val logger = HyLogger("${Color(204, 255, 51).foreground()}$pluginName")

    /**
     * 插件配置目录
     */
    val pluginDir: File
        get() = File(Files.PLUGINS.file, pluginName)

    /**
     * 插件启动
     */
    abstract suspend fun onPluginStart()

    /**
     * 插件关闭
     */
    abstract suspend fun onPluginStop()

    /**
     * 获取yml格式配置文件
     */
    fun getYamlConfig(name: String, default: Consumer<HyConfig>): HyConfig {
        val file = File(pluginDir, name)
        val config = HyConfig(file, false, true)
        if (!file.exists()) {
            config.let {
                default.accept(it)
                it.save()
            }
        }
        config.load()
        return config

    }

    /**
     * 获取json格式配置文件
     */
    fun getJsonConfig(name: String, default: Consumer<JsonObject>): JsonElement {
        val file = File(pluginDir, name)
        if (file.exists()) return Gson().fromJson(file.readText())
        val json = JsonObject()
        default.accept(json)
        file.writeText(Util.GSON.toJson(json))
        return json
    }
}