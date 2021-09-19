package net.cjsah.console.plugin

import net.cjsah.console.Console
import net.cjsah.console.Permission
import net.cjsah.console.command.Command
import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.argument.LongArgument
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.util.Util

class ConsolePlugin private constructor(): Plugin(
    "ConsolePlugin",
    "0.0.1",
    false,
    listOf("Cjsah")
) {

    companion object {
        private val plugin = ConsolePlugin()
        fun get() = plugin
    }

    override fun onPluginStart() {
        // stop
        CommandManager.register(CommandManager.literal("stop").requires{source ->
            source is ConsoleCommandSource
        }.executes("") {
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
        CommandManager.register(CommandManager.literal("permission").requires{source -> source.hasPermission(
            Permission.ADMIN
        )}.then(CommandManager.literal("set").then(CommandManager.argument("id", LongArgument.longArg()).then(CommandManager.literal("user").executes("") { context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.USER)
            return@executes Command.SUCCESSFUL
        }).then(CommandManager.literal("helper").executes("") {context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.HELPER)
            return@executes Command.SUCCESSFUL
        }).then(CommandManager.literal("admin").requires{source -> source.hasPermission(Permission.OWNER)}.executes("") { context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.ADMIN)
            return@executes Command.SUCCESSFUL
        }))))
    }
}