@file:Suppress("MemberVisibilityCanBePrivate")

package net.cjsah.console.plugin

import net.cjsah.console.ConsoleFiles
import net.cjsah.console.exceptions.PluginException
import java.io.File

abstract class Plugin {
    private lateinit var info: PluginInformation
    private var init = false

    fun init(info: PluginInformation) {
        if (!init) {
            this.info = info
            if (info.hasConfig && (!getPluginDir().exists() || getPluginDir().isDirectory)) { getPluginDir().mkdirs() }
            onPluginLoad()
            init = true
        } else throw PluginException("插件已经初始化, 请勿再次初始化")
    }

    fun getInfo() = info

    /**
     * 插件加载时执行
     *
     * 此时 [bot = null][net.cjsah.console.Console.bot]
     */
    protected abstract fun onPluginLoad()

    /**
     * 机器人登陆后执行
     */
    abstract fun onBotStarted()

    /**
     * 机器人下线后执行
     */
    abstract fun onBotStopped()

    /**
     * 插件卸载时执行
     */
    abstract fun onPluginUnload()

    /**
     * 获取插件配置目录
     * @return 配置文件夹
     */
    fun getPluginDir(): File {
        if (!info.hasConfig) throw PluginException("此插件没有配置文件")
        return File(ConsoleFiles.PLUGINS.file, info.id)
    }

    override fun toString() = "${info.name} (${info.id}) v${info.version}"
}