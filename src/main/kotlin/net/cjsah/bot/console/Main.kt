package net.cjsah.bot.console

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.bot.console.util.LogAppender
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility

fun main() {
//    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    if (System.getProperty("nocolor") != null) HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED

    Files.init()

    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())

    Console.start(123456, "123456", false)

}
