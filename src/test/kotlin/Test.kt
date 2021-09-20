import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import net.cjsah.console.Console
import net.cjsah.console.Files
import net.cjsah.console.Permission
import net.cjsah.console.command.Command
import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.Dispatcher
import net.cjsah.console.command.argument.LongArgument
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.util.LogAppender
import net.cjsah.console.util.Util
import org.hydev.logger.HyLoggerConfig

fun main() {
//    System.setProperty("mirai.no-desktop", "")
//    HyLoggerConfig.appenders[0] = LogAppender()
//
//    Files.init()
//
//    Console.permissions = Gson().fromJson(Files.PERMISSIONS.file.readText())
//
//    Console.start(123456, "123456", false)

    val dispatcher = Dispatcher()

    dispatcher.register(CommandManager.literal("stop").requires{ source ->
        source is ConsoleCommandSource
    }.executes("停止机器人") {
        Console.stop()
        return@executes Command.SUCCESSFUL
    })

//        // help
//        CommandManager.register(CommandManager.literal("help").executes("") { context ->
//            val map: Map<CommandNode, String> = dispatcher.getSmartUsage(context.source)
//            for (value in map.values) {
//                (context.source).sendFeedBack("/$value")
//            }
//        })

    // permission
    dispatcher.register(CommandManager.literal("permission").requires{ source -> source.hasPermission(
        Permission.ADMIN
    )}.then(CommandManager.literal("set").then(CommandManager.argument("id", LongArgument.longArg()).then(CommandManager.literal("user").executes("设置 <id> 为 user 权限") { context ->
        Util.setPermission(LongArgument.getLong(context, "id"), Permission.USER)
        return@executes Command.SUCCESSFUL
    }).then(CommandManager.literal("helper").executes("设置 <id> 为 helper 权限") { context ->
        Util.setPermission(LongArgument.getLong(context, "id"), Permission.HELPER)
        return@executes Command.SUCCESSFUL
    }).then(CommandManager.literal("admin").requires{ source -> source.hasPermission(Permission.OWNER)}.executes("设置 <id> 为 admin 权限") { context ->
        Util.setPermission(LongArgument.getLong(context, "id"), Permission.ADMIN)
        return@executes Command.SUCCESSFUL
    }))))



    val help = dispatcher.getHelp(ConsoleCommandSource(Console))
    help.entries.forEach {
        println("${it.key}\t${it.value}")
    }

}
