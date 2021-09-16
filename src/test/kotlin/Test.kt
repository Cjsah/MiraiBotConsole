import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.console.Console
import net.cjsah.console.Files
import net.cjsah.console.util.LogAppender
import org.hydev.logger.HyLoggerConfig

fun main() {
    System.setProperty("mirai.no-desktop", "")
    HyLoggerConfig.appenders[0] = LogAppender()

    Files.init()

    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())

    Console.start(123456, "123456", false)
}
