package net.cjsah.console

import net.cjsah.console.Console.permissions
import net.cjsah.console.command.Command
import net.cjsah.console.command.CommandManager.argument
import net.cjsah.console.command.CommandManager.literal
import net.cjsah.console.command.CommandManager.register
import net.cjsah.console.command.argument.LongArgument
import net.cjsah.console.command.argument.PluginArgument
import net.cjsah.console.command.source.ConsoleCommandSource
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User

object ConsoleCommand {
    @JvmStatic
    internal fun register() {
        // help
        register { dispatcher ->
            dispatcher.register(
                literal("help").executes("打开帮助") { context ->
                    val helps = dispatcher.getHelp(context.getSource())
                    if (context.getSource() is ConsoleCommandSource) {
                        helps.entries.forEach { context.getSource().sendFeedBack("${it.key}\t${it.value}") }
                    } else {
                        val collect = helps.entries.joinToString("\n") { "${it.key}\t${it.value}" }
                        context.getSource().sendFeedBack(collect)
                    }
                    Command.SUCCESSFUL
                }
            )
        }

        // stop
        register { dispatcher ->
            dispatcher.register(
                literal("stop").requires { source -> source is ConsoleCommandSource }
                    .executes("停止服务器") {
                        Console.stop()
                        Command.SUCCESSFUL
                    }
            )
        }

        // permission
        register { dispatcher ->
            dispatcher.register(
                literal("permission").requires { source ->
                    (source.getSource() !is Contact || source.getSource() is User) &&
                            source.hasPermission(Permissions.PermissionType.ADMIN)
                }.then(literal("reload")
                    .executes("重载权限") { context ->
                        permissions.reload()
                        context.getSource().sendFeedBack("重载成功")
                        Command.SUCCESSFUL
                    }
                ).then(argument("plugin", PluginArgument.plugin())
                    .then(literal("set")
                        .then(literal("whitelist")
                            .executes("设置插件为白名单模式") { context ->
                                val plugin = PluginArgument.getPlugin(context, "plugin")
                                context.getSource().sendFeedBack(permissions.setListType(plugin, true))
                                Command.SUCCESSFUL
                            }
                        ).then(literal("blacklist")
                            .executes("设置插件为黑名单模式(默认)") { context ->
                                val plugin = PluginArgument.getPlugin(context, "plugin")
                                context.getSource().sendFeedBack(permissions.setListType(plugin, false))
                                Command.SUCCESSFUL
                            }
                        )
                    ).then(literal("status")
                        .executes("权限状态") { context ->
                            val plugin = PluginArgument.getPlugin(context, "plugin")
                            var content = "插件 ${plugin.info.name} 的权限状态:\n"
                            content += "模式: ${if (permissions.isWhite(plugin)) "白" else "黑"}名单\n"
                            val wul = permissions.getWU(plugin).map { obj -> obj.toString() }
                            val wgl = permissions.getWG(plugin).map { obj -> obj.toString() }
                            val bul = permissions.getBU(plugin).map { obj -> obj.toString() }
                            val bgl = permissions.getBG(plugin).map { obj -> obj.toString() }

                            if (wul.isEmpty() && wgl.isEmpty()) content += "白名单: 空\n"
                            else {
                                if (wul.isNotEmpty()) content += "用户白名单: \n${wul.joinToString("\n")}\n"
                                if (wgl.isNotEmpty()) content += "群白名单: \n${wgl.joinToString("\n")}\n"
                            }
                            if (bul.isEmpty() && bgl.isEmpty()) content += "黑名单: 空\n"
                            else {
                                if (bul.isNotEmpty()) content += "用户黑名单: \n${bul.joinToString("\n")}\n"
                                if (bgl.isNotEmpty()) content += "群黑名单: \n${bgl.joinToString("\n")}\n"
                            }
                            context.getSource().sendFeedBack(content.substring(0, content.length-1))
                            Command.SUCCESSFUL
                        }
                    ).then(literal("whitelist")
                        .then(literal("add")
                            .then(literal("user")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("添加用户到插件白名单") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.addToList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = true,
                                                isUser = true
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            ).then(literal("group")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("添加群组到插件白名单") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.addToList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = true,
                                                isUser = false
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            )
                        ).then(literal("remove")
                            .then(literal("user")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("从插件白名单移除用户") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.removeFromList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = true,
                                                isUser = true
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            ).then(literal("group")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("从插件白名单移除群组") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.removeFromList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = true,
                                                isUser = false
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            )
                        )
                    ).then(literal("blacklist")
                        .then(literal("add")
                            .then(literal("user")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("添加用户到插件黑名单") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.addToList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = false,
                                                isUser = true
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            ).then(literal("group")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("添加群组到插件黑名单") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.addToList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = false,
                                                isUser = false
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            )
                        ).then(literal("remove")
                            .then(literal("user")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("从插件黑名单移除用户") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.removeFromList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = false,
                                                isUser = true
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            ).then(literal("group")
                                .then(argument("id", LongArgument.longArg())
                                    .executes("从插件黑名单移除群组") { context ->
                                        context.getSource().sendFeedBack(
                                            permissions.removeFromList(
                                                PluginArgument.getPlugin(context, "plugin"),
                                                LongArgument.getLong(context, "id"),
                                                isWhite = false,
                                                isUser = false
                                            )
                                        )
                                        Command.SUCCESSFUL
                                    }
                                )
                            )
                        )
                    )
                )
            )
        }

    }
}