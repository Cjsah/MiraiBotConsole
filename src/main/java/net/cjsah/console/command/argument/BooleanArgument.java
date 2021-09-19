package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.CommandException;

public class BooleanArgument implements Argument<Boolean> {

    private BooleanArgument() {
    }

    public static BooleanArgument bool() {
        return new BooleanArgument();
    }

    public static Boolean getBoolean(CommandContext context, String name) {
        return context.getArgument(name, boolean.class);
    }

    @Override
    public Boolean parse(StringReader reader) throws CommandException {
        return reader.readBoolean();
    }
}
