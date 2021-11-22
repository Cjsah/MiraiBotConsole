package net.cjsah.console;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.cjsah.console.command.Command;
import net.cjsah.console.command.CommandManager;
import net.cjsah.console.command.argument.LongArgument;
import net.cjsah.console.command.argument.PluginArgument;
import net.cjsah.console.command.source.ConsoleCommandSource;
import net.cjsah.console.plugin.Plugin;

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
        CommandManager.register(dispatcher -> dispatcher.register(CommandManager.literal("permission").then(CommandManager.argument("plugin", PluginArgument.plugin()).then(CommandManager.literal("whitelist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到插件白名单", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到插件白名单", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, false));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件白名单移除用户", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.removePermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件白名单移除群组", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.removePermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), true, false));
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("blacklist").then(CommandManager.literal("add").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加用户到插件黑名单", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("添加群组到插件黑名单", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.setPermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, false));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("remove").then(CommandManager.literal("user").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件黑名单移除用户", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.removePermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, true));
            return Command.SUCCESSFUL;
        }))).then(CommandManager.literal("group").then(CommandManager.argument("id", LongArgument.longArg()).executes("从插件黑名单移除群组", context -> {
            context.getSource().sendFeedBack(Util.INSTANCE.removePermission(PluginArgument.getPlugin(context, "plugin"), LongArgument.getLong(context, "id"), false, false));
            return Command.SUCCESSFUL;
        }))))).then(CommandManager.literal("status").executes("权限状态", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            JsonObject json = Console.permissions.get(plugin.getInfo().getId()).getAsJsonObject();
            String content = String.format("插件 %s 的权限状态:\n模式: %s名单\n", plugin.getInfo().getName(), json.get("whitelist").getAsBoolean() ? "白" : "黑");
            List<String> wul = getList(json.get("white").getAsJsonObject().get("user").getAsJsonArray());
            List<String> wgl = getList(json.get("white").getAsJsonObject().get("group").getAsJsonArray());
            List<String> bul = getList(json.get("black").getAsJsonObject().get("user").getAsJsonArray());
            List<String> bgl = getList(json.get("black").getAsJsonObject().get("group").getAsJsonArray());
            if (wul.isEmpty() && wgl.isEmpty()) content += "白名单: 空\n";
            else content += String.format("白名单: \n%s\n%s\n", String.join("\n", wul), String.join("\n", wgl));
            if (bul.isEmpty() && bgl.isEmpty()) content += "黑名单: 空\n";
            else content += String.format("黑名单: \n%s\n%s", String.join("\n", bul), String.join("\n", bgl));
            context.getSource().sendFeedBack(content);
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("set").then(CommandManager.literal("whitelist").executes("设置插件为白名单模式", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            Console.permissions.get(plugin.getInfo().getId()).getAsJsonObject().addProperty("whitelist", true);
            Util.INSTANCE.save(ConsoleFiles.PERMISSIONS.getFile(), Util.INSTANCE.getGSON().toJson(Console.permissions));
            context.getSource().sendFeedBack(String.format("已将插件 %s 设为白名单模式", plugin.getInfo().getName()));
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("blacklist").executes("设置插件为黑名单模式", context -> {
            Plugin plugin = PluginArgument.getPlugin(context, "plugin");
            Console.permissions.get(plugin.getInfo().getId()).getAsJsonObject().addProperty("whitelist", false);
            Util.INSTANCE.save(ConsoleFiles.PERMISSIONS.getFile(), Util.INSTANCE.getGSON().toJson(Console.permissions));
            context.getSource().sendFeedBack(String.format("已将插件 %s 设为黑名单模式", plugin.getInfo().getName()));
            return Command.SUCCESSFUL;
        })))).then(CommandManager.literal("reload").executes("重载权限", context -> {
            Console.permissions = Util.INSTANCE.fromJson(ConsoleFiles.PERMISSIONS.getFile());
            context.getSource().sendFeedBack("重载成功");
            return Command.SUCCESSFUL;
        }))));
    }

    private static List<String> getList(JsonArray array) {
        List<String> list = Lists.newArrayList();
        array.forEach(i -> list.add(String.valueOf(i.getAsLong())));
        return list;
    }
}
