package net.cjsah.console;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.CommandManager;
import net.cjsah.console.command.argument.LongArgument;
import net.cjsah.console.command.source.ConsoleCommandSource;

import java.util.Map;

public class ConsoleCommand {
    protected static void register() {
        // stop
        CommandManager.register((dispatcher) -> dispatcher.register(CommandManager.literal("stop").requires(source ->
                source instanceof ConsoleCommandSource
        ).executes("停止服务器", context -> {
            Console.INSTANCE.stop$MiraiBotConsole();
            return Command.SUCCESSFUL;
        })));

        // help
        CommandManager.register((dispatcher) -> dispatcher.register(CommandManager.literal("help").executes("打开帮助", context -> {
            Map<String, String> helps = dispatcher.getHelp(context.getSource());
            for (Map.Entry<String, String> help : helps.entrySet()) {
                context.getSource().sendFeedBack(String.format("%s\t%s\n", help.getKey(), help.getValue()));
            }
            return Command.SUCCESSFUL;
        })));

        // permission
        CommandManager.register((dispatcher) -> dispatcher.register(CommandManager.literal("permission").requires(source ->
                source.hasPermission(Permission.ADMIN)
        ).then(CommandManager.literal("set").then(CommandManager.argument("id", LongArgument.longArg()).then(CommandManager.literal("user").executes("设置 <id> 为 user 权限", context -> {
            Util.INSTANCE.setPermission(LongArgument.getLong(context, "id"), Permission.USER);
            return Command.SUCCESSFUL;
        }).then(CommandManager.literal("helper").executes("设置 <id> 为 helper 权限", context -> {
            Util.INSTANCE.setPermission(LongArgument.getLong(context, "id"), Permission.HELPER);
            return Command.SUCCESSFUL;
        })).then(CommandManager.literal("admin").requires(source -> source.hasPermission(Permission.OWNER)).executes("设置 <id> 为 admin 权限", context -> {
            Util.INSTANCE.setPermission(LongArgument.getLong(context, "id"), Permission.ADMIN);
            return Command.SUCCESSFUL;
        })))))));
    }
}
