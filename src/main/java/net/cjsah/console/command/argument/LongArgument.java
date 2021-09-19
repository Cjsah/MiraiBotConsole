package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

public class LongArgument implements Argument<Long> {
    private final long min;
    private final long max;

    private LongArgument(final long min, final long max) {
        this.min = min;
        this.max = max;
    }

    public static LongArgument longArg() {
        return longArg(Long.MIN_VALUE);
    }

    public static LongArgument longArg(final long min) {
        return longArg(min, Long.MAX_VALUE);
    }

    public static LongArgument longArg(final long min, final long max) {
        return new LongArgument(min, max);
    }

    public static long getLong(CommandContext context, String name) {
        return context.getArgument(name, long.class);
    }

    public long getMin() {
        return this.min;
    }

    public long getMax() {
        return this.max;
    }

    @Override
    public Long parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        long result = reader.readLong();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.longTooLow().createWithContext(reader, result, this.max);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.longTooHigh().createWithContext(reader, result, this.max);
        }
        return result;
    }
}
