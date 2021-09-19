package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

public class DoubleArgument implements Argument<Double> {
    private final double min;
    private final double max;

    private DoubleArgument(final double min, final double max) {
        this.min = min;
        this.max = max;
    }

    public static DoubleArgument doubleArg() {
        return doubleArg(Double.MIN_VALUE);
    }

    public static DoubleArgument doubleArg(final double min) {
        return doubleArg(min, Double.MAX_VALUE);
    }

    public static DoubleArgument doubleArg(final double min, final double max) {
        return new DoubleArgument(min, max);
    }

    public static double getDouble(CommandContext context, String name) {
        return context.getArgument(name, double.class);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    @Override
    public Double parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        double result = reader.readDouble();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.doubleTooLow().createWithContext(reader, result, this.max);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.doubleTooHigh().createWithContext(reader, result, this.max);
        }
        return result;
    }
}
