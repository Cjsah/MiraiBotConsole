package net.cjsah.bot.console.command.exceptions;

import net.cjsah.bot.console.command.ImmutableStringReader;

import java.util.function.Function;

public class DynamicCommandExceptionType implements CommandExceptionType {
    private final Function<Object, String> function;

    public DynamicCommandExceptionType(final Function<Object, String> function) {
        this.function = function;
    }

    public CommandException create(final Object arg) {
        return new CommandException(this, function.apply(arg));
    }

    public CommandException createWithContext(final ImmutableStringReader reader, final Object arg) {
        return new CommandException(this, function.apply(arg), reader.getString(), reader.getCursor());
    }

}
