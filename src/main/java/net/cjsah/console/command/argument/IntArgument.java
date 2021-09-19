package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

public class IntArgument implements Argument<Integer> {
    private final int min;
    private final int max;

    private IntArgument(final int min, final int max) {
        this.min = min;
        this.max = max;
    }

    public static IntArgument integer() {
        return integer(Integer.MIN_VALUE);
    }

    public static IntArgument integer(final int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    public static IntArgument integer(final int min, final int max) {
        return new IntArgument(min, max);
    }

    public static int getInteger(CommandContext context, String name) {
        return context.getArgument(name, int.class);
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    @Override
    public Integer parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        int result = reader.readInt();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.integerTooLow().createWithContext(reader, result, this.max);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.integerTooHigh().createWithContext(reader, result, this.max);
        }
        return result;
    }
}
