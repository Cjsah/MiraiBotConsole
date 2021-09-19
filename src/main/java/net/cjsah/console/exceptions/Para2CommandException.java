package net.cjsah.console.exceptions;

import net.cjsah.console.command.StringReader;

public class Para2CommandException implements CommandExceptionType {
    private final Function function;

    public Para2CommandException(final Function function) {
        this.function = function;
    }

    public CommandException create(Object a, Object b) {
        return new CommandException(this, this.function.apply(a, b));
    }

    public CommandException createWithContext(StringReader reader, Object a, Object b) {
        return new CommandException(this, this.function.apply(a, b), reader.getString(), reader.getCursor());
    }

    interface Function {
        String apply(Object a, Object b);
    }
}
