package net.cjsah.bot.console.command.exceptions;

import net.cjsah.bot.console.command.ImmutableStringReader;

public class Dynamic2CommandExceptionType implements CommandExceptionType {
    private final Function function;

    public Dynamic2CommandExceptionType(final Function function) {
        this.function = function;
    }

    public CommandException create(final Object a, final Object b) {
        return new CommandException(this, function.apply(a, b));
    }

    public CommandException createWithContext(final ImmutableStringReader reader, final Object a, final Object b) {
        return new CommandException(this, function.apply(a, b), reader.getString(), reader.getCursor());
    }

    public interface Function {
        String apply(Object a, Object b);
    }

}
