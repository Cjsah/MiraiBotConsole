package net.cjsah.console;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.CommandManager;
import net.cjsah.console.command.argument.LongArgument;
import net.cjsah.console.command.argument.PluginArgument;
import net.cjsah.console.command.source.ConsoleCommandSource;
import net.cjsah.console.plugin.Plugin;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;

import java.util.List;
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
        CommandManager.register(dispatcher -> dispatcher.register(CommandManager.literal("permission").requires(source ->
                (!(source.getSource() instanceof Contact) || source.getSource() instanceof User) && source.hasPermission(Permissions.PermissionType.ADMIN)
        ).then(CommandManager.argument("plugin", PluginArgument.plugin()).then(CommandManager.literal("whitelist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到插件白名单", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().addToList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到插件白名单", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().addToList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, false));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件白名单移除用户", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().removeFromList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件白名单移除群组", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().removeFromList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, false));
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("blacklist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到插件黑名单", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().addToList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到插件黑名单", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().addToList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, false));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件黑名单移除用户", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().removeFromList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件黑名单移除群组", context -> {
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().removeFromList(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, false));
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("status").executes("权限状态", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            String content = String.format("插件 %s 的权限状态:\n模式: %s名单\n", plugin.getInfo().getName(), Console.INSTANCE.getPermissions().isWhite(plugin) ? "白" : "黑");
            List<String> wul = Console.INSTANCE.getPermissions().getWU(plugin).stream().map(String::valueOf).collect(Collectors.toList());
            List<String> wgl = Console.INSTANCE.getPermissions().getWG(plugin).stream().map(String::valueOf).collect(Collectors.toList());
            List<String> bul = Console.INSTANCE.getPermissions().getBU(plugin).stream().map(String::valueOf).collect(Collectors.toList());
            List<String> bgl = Console.INSTANCE.getPermissions().getBG(plugin).stream().map(String::valueOf).collect(Collectors.toList());
            if (wul.isEmpty() && wgl.isEmpty()) content += "白名单: 空\n";
            else {
                if (!wul.isEmpty()) content += String.format("用户白名单: \n%s\n", String.join("\n", wul));
                if (!wgl.isEmpty()) content += String.format("群白名单: \n%s\n", String.join("\n", wgl));
            }
            if (bul.isEmpty() && bgl.isEmpty()) content += "黑名单: 空\n";
            else {
                if (!bul.isEmpty()) content += String.format("用户黑名单: \n%s\n", String.join("\n", bul));
                if (!bgl.isEmpty()) content += String.format("群黑名单: \n%s\n", String.join("\n", bgl));
            }
            context.getSource().sendFeedBack(content);
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("set").then(CommandManager.literal("whitelist").executes("设置插件为白名单模式", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().setListType(plugin, true));
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("blacklist").executes("设置插件为黑名单模式(默认)", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            context.getSource().sendFeedBack(Console.INSTANCE.getPermissions().setListType(plugin, false));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("reload").executes("重载权限", context -> {
            Console.INSTANCE.getPermissions().reload();
            context.getSource().sendFeedBack("重载成功");
            return Command.SUCCESSFUL;
        }))));
    }
}
