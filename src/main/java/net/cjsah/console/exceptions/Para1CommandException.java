package net.cjsah.console.exceptions;

import net.cjsah.console.command.StringReader;

import java.util.function.Function;

public class Para1CommandException implements CommandExceptionType {
    private final Function<Object, String> function;

    public Para1CommandException(final Function<Object, String> function) {
        this.function = function;
    }

    public CommandException create(Object arg) {
        return new CommandException(this, this.function.apply(arg));
    }

    public CommandException createWithContext(StringReader reader, Object arg) {
        return new CommandException(this, this.function.apply(arg), reader.getString(), reader.getCursor());
    }


}
