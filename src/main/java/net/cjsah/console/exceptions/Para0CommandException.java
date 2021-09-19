package net.cjsah.console.exceptions;

import net.cjsah.console.command.StringReader;

public class Para0CommandException implements CommandExceptionType{
    private final String message;

    public Para0CommandException(final String message) {
        this.message = message;
    }

    public CommandException create() {
        return new CommandException(this, this.message);
    }

    public CommandException createWithContext(StringReader reader) {
        return new CommandException(this, this.message, reader.getString(), reader.getCursor());
    }

    @Override
    public String toString() {
        return this.message;
    }
}
