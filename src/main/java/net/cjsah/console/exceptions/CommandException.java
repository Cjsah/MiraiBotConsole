package net.cjsah.console.exceptions;

public class CommandException extends Exception {
    public static boolean ENABLE_COMMAND_STACK_TRACES = true;
    public static int CONTEXT_AMOUNT = 100;
    private final CommandExceptionType type;
    private final String message;
    private final String input;
    private final int cursor;

    public CommandException(CommandExceptionType type, String message) {
        super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.message = message;
        this.type = type;
        this.input = null;
        this.cursor = -1;
    }

    public CommandException(CommandExceptionType type, String message, String input, int cursor) {
        super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.message = message;
        this.type = type;
        this.input = input;
        this.cursor = cursor;
    }

    @Override
    public String getMessage() {
        if (this.input == null || this.cursor < 0) {
            return this.message;
        }
        final StringBuilder builder = new StringBuilder();
        final int cursor = Math.min(this.input.length(), this.cursor);
        if (cursor > CONTEXT_AMOUNT) {
            builder.append("...");
        }
        builder.append(this.input, Math.max(0, cursor - CONTEXT_AMOUNT), cursor);
        builder.append("<--[HERE]");
        return this.message + " at position " + cursor + ": " + builder;
    }

    public CommandExceptionType getType() {
        return this.type;
    }
}
