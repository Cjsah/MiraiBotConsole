package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.CommandException;

public class StringArgument implements Argument<String> {
    private final boolean space;

    private StringArgument(final boolean space) {
        this.space = space;
    }

    public static StringArgument word() {
        return new StringArgument(false);
    }

    public static StringArgument string() {
        return new StringArgument(true);
    }

    public static String getString(CommandContext context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandException {
        if (this.space) {
            String text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        }else return reader.readUnquotedString();
    }
}
