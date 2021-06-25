
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.bot.console.*
import net.cjsah.bot.console.util.LogAppender
import org.hydev.logger.HyLoggerConfig


fun main() {
//    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()
    val logger = Console.logger
    val config = Account.get()

    if (Files.init()) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())

    Console.start(config.getLong("account"), config.getString("password"))
}
