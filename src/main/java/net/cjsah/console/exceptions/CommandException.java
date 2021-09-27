package net.cjsah.console.exceptions;

public class CommandException extends Exception {
    public static boolean ENABLE_COMMAND_STACK_TRACES = true;
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

    public CommandExceptionType getType() {
        return this.type;
    }
}
