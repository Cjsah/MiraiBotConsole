package net.cjsah.console.plugin

import net.cjsah.console.Console
import net.cjsah.console.Permission
import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.CommandManager.dispatcher
import net.cjsah.console.command.arguments.base.LongArgument
import net.cjsah.console.command.source.ConsoleCommandSource
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.events.CommandRegistration
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
        CommandRegistration.EVENT.register(CommandManager.literal("stop").requires{source ->
            source is ConsoleCommandSource
        }.executes {
            Console.stop()
        })

        // help
        CommandRegistration.EVENT.register(CommandManager.literal("help").executes{ context ->
            val map: Map<CommandNode, String> = dispatcher.getSmartUsage(context.source)
            for (value in map.values) {
                (context.source).sendFeedBack("/$value")
            }
        })

        // permission
        CommandRegistration.EVENT.register(CommandManager.literal("permission").requires{source -> source.hasPermission(
            Permission.ADMIN
        )}.then(CommandManager.literal("set").then(CommandManager.argument("id", LongArgument.long()).then(CommandManager.literal("user").executes{ context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.USER)
        }).then(CommandManager.literal("helper").executes{context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.HELPER)
        }).then(CommandManager.literal("admin").requires{source -> source.hasPermission(Permission.OWNER)}.executes{ context ->
            Util.setPermission(LongArgument.getLong(context, "id"), Permission.ADMIN)
        }))))
    }
}