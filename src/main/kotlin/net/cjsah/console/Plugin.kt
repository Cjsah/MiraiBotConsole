package net.cjsah.console

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import org.hydev.logger.HyLogger
import org.hydev.logger.foreground
import java.awt.Color
import java.io.File

@Suppress("unused")
abstract class Plugin(
    val pluginName: String,
    val pluginVersion: String,
    val hasConfig: Boolean,
    val pluginAuthors: List<String>?
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