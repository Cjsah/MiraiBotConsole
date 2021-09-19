package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

public class FloatArgument implements Argument<Float> {
    private final float min;
    private final float max;

    private FloatArgument(final float min, final float max) {
        this.min = min;
        this.max = max;
    }

    public static FloatArgument floatArg() {
        return floatArg(Float.MIN_VALUE);
    }

    public static FloatArgument floatArg(final float min) {
        return floatArg(min, Float.MAX_VALUE);
    }

    public static FloatArgument floatArg(final float min, final float max) {
        return new FloatArgument(min, max);
    }

    public static double getFloat(CommandContext context, String name) {
        return context.getArgument(name, float.class);
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    @Override
    public Float parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        float result = reader.readFloat();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.floatTooLow().createWithContext(reader, result, this.max);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.floatTooHigh().createWithContext(reader, result, this.max);
        }
        return result;
    }
}
