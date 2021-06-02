package net.cjsah.bot.console.command.exceptions;

import net.cjsah.bot.console.command.ImmutableStringReader;

public class Para0CommandExceptionType implements CommandExceptionType {
    private final String message;

    public Para0CommandExceptionType(final String message) {
        this.message = message;
    }

    public CommandException create() {
        return new CommandException(this, message);
    }

    public CommandException createWithContext(final ImmutableStringReader reader) {
        return new CommandException(this, message, reader.getString(), reader.getCursor());
    }

    @Override
    public String toString() {
        return message;
    }
}
