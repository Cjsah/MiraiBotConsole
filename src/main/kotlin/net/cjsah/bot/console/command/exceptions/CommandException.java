package net.cjsah.bot.console.command.exceptions;

public class CommandException extends Exception {

    public static final int CONTEXT_AMOUNT = 10;
    public static boolean ENABLE_COMMAND_STACK_TRACES = true;
    public static BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();

    private final CommandExceptionType type;
    private final String message;
    private final String input;
    private final int cursor;

    public CommandException(final CommandExceptionType type, final String message) {
        super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.type = type;
        this.message = message;
        this.input = null;
        this.cursor = -1;
    }

    public CommandException(final CommandExceptionType type, final String message, final String input, final int cursor) {
        super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.type = type;
        this.message = message;
        this.input = input;
        this.cursor = cursor;
    }

    @Override
    public String getMessage() {
        String message = this.message;
        final String context = getContext();
        if (context != null) {
            message += " at position " + cursor + ": " + context;
        }
        return message;
    }

    public String getRawMessage() {
        return message;
    }

    public String getContext() {
        if (input == null || cursor < 0) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        final int cursor = Math.min(input.length(), this.cursor);

        if (cursor > CONTEXT_AMOUNT) {
            builder.append("...");
        }

        builder.append(input, Math.max(0, cursor - CONTEXT_AMOUNT), cursor);
        builder.append("<--[HERE]");

        return builder.toString();
    }

    public CommandExceptionType getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public int getCursor() {
        return cursor;
    }

}
