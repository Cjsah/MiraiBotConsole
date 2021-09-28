package net.cjsah.console.command;

import net.cjsah.console.Console;
import net.cjsah.console.command.argument.Argument;
import net.cjsah.console.command.builder.LiteralArgumentBuilder;
import net.cjsah.console.command.builder.RequiredArgumentBuilder;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.exceptions.CommandException;

import java.util.function.Consumer;

public class CommandManager {

    private static final Dispatcher DISPATCHER = new Dispatcher();

    public static LiteralArgumentBuilder literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<T> argument(String literal, Argument<T> type) {
        return RequiredArgumentBuilder.argument(literal, type);
    }

    public static int execute(String command, CommandSource<?> source) {
        try {
            return DISPATCHER.execute(command, source);
        }catch (CommandException e) {
            if (e.getMessage() != null) Console.INSTANCE.getLogger().error(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void register(Consumer<Dispatcher> command) {
        command.accept(DISPATCHER);
    }

}
