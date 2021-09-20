package net.cjsah.console

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.console.util.LogAppender
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility

fun main() {
//    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    if (System.getProperty("nocolor") != null) HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED

    val logger = net.cjsah.console.Console.logger
    val config = Account.get()

    if (Files.init()) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())

    Console.start(config.getLong("account"), config.getString("password"))

}