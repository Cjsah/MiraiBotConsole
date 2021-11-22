package net.cjsah.console;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.CommandManager;
import net.cjsah.console.command.argument.LongArgument;
import net.cjsah.console.command.argument.PluginArgument;
import net.cjsah.console.command.source.ConsoleCommandSource;

import java.util.Map;
import java.util.stream.Collectors;

public class ConsoleCommand {
    protected static void register() {
        // stop
        CommandManager.register((dispatcher) -> dispatcher.register(CommandManager.literal("stop").requires(source ->
                source instanceof ConsoleCommandSource
        ).executes("停止服务器", context -> {
            Console.INSTANCE.stop();
            return Command.SUCCESSFUL;
        })));

        // help
        CommandManager.register((dispatcher) -> dispatcher.register(CommandManager.literal("help").executes("打开帮助", context -> {
            Map<String, String> helps = dispatcher.getHelp(context.getSource());
            if (context.getSource() instanceof ConsoleCommandSource) {
                for (Map.Entry<String, String> help : helps.entrySet()) {
                    context.getSource().sendFeedBack(String.format("%s\t%s", help.getKey(), help.getValue()));
                }
            } else {
                String collect = helps.entrySet().stream().map((entry) -> String.format("%s\t%s", entry.getKey(), entry.getValue())).collect(Collectors.joining("\n"));
                context.getSource().sendFeedBack(collect);
            }
            return Command.SUCCESSFUL;
        })));

        // permission
        CommandManager.register(dispatcher -> dispatcher.register(CommandManager.literal("permission").then(CommandManager.argument("plugin", PluginArgument.plugin()).then(CommandManager.literal("whitelist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到mod白名单", context -> {
            Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, true);
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到mod白名单", context -> {
            Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, false);
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从mod白名单移除用户", context -> {
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从mod白名单移除群组", context -> {
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("blacklist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到mod黑名单", context -> {
            Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, true);
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到mod黑名单", context -> {
            Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, false);
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从mod黑名单移除用户", context -> {
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从mod黑名单移除群组", context -> {
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("status").executes("权限状态", context -> {
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("set").then(CommandManager.literal("whitelist").executes("设为白名单模式", context -> {
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("blacklist").executes("设为黑名单模式", context -> {
            return Command.SUCCESSFUL;
        }))))));


    }
}
