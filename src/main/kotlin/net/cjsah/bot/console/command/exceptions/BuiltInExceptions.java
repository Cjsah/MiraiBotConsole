package net.cjsah.bot.console.command.exceptions;

public class BuiltInExceptions implements BuiltInExceptionProvider {
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_LOW = new Dynamic2CommandExceptionType((found, min) -> "Double must not be less than " + min + ", found " + found);
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_HIGH = new Dynamic2CommandExceptionType((found, max) -> "Double must not be more than " + max + ", found " + found);

    private static final Dynamic2CommandExceptionType FLOAT_TOO_LOW = new Dynamic2CommandExceptionType((found, min) -> "Float must not be less than " + min + ", found " + found);
    private static final Dynamic2CommandExceptionType FLOAT_TOO_HIGH = new Dynamic2CommandExceptionType((found, max) -> "Float must not be more than " + max + ", found " + found);

    private static final Dynamic2CommandExceptionType INTEGER_TOO_LOW = new Dynamic2CommandExceptionType((found, min) -> "Integer must not be less than " + min + ", found " + found);
    private static final Dynamic2CommandExceptionType INTEGER_TOO_HIGH = new Dynamic2CommandExceptionType((found, max) -> "Integer must not be more than " + max + ", found " + found);

    private static final Dynamic2CommandExceptionType LONG_TOO_LOW = new Dynamic2CommandExceptionType((found, min) -> "Long must not be less than " + min + ", found " + found);
    private static final Dynamic2CommandExceptionType LONG_TOO_HIGH = new Dynamic2CommandExceptionType((found, max) -> "Long must not be more than " + max + ", found " + found);

    private static final DynamicCommandExceptionType LITERAL_INCORRECT = new DynamicCommandExceptionType(expected -> "Expected literal " + expected);

    private static final Para0CommandExceptionType READER_EXPECTED_START_OF_QUOTE = new Para0CommandExceptionType("Expected quote to start a string");
    private static final Para0CommandExceptionType READER_EXPECTED_END_OF_QUOTE = new Para0CommandExceptionType("Unclosed quoted string");
    private static final DynamicCommandExceptionType READER_INVALID_ESCAPE = new DynamicCommandExceptionType(character -> "Invalid escape sequence '" + character + "' in quoted string");
    private static final DynamicCommandExceptionType READER_INVALID_BOOL = new DynamicCommandExceptionType(value -> "Invalid bool, expected true or false but found '" + value + "'");
    private static final DynamicCommandExceptionType READER_INVALID_INT = new DynamicCommandExceptionType(value -> "Invalid integer '" + value + "'");
    private static final Para0CommandExceptionType READER_EXPECTED_INT = new Para0CommandExceptionType("Expected integer");
    private static final DynamicCommandExceptionType READER_INVALID_LONG = new DynamicCommandExceptionType(value -> "Invalid long '" + value + "'");
    private static final Para0CommandExceptionType READER_EXPECTED_LONG = new Para0CommandExceptionType(("Expected long"));
    private static final DynamicCommandExceptionType READER_INVALID_DOUBLE = new DynamicCommandExceptionType(value -> "Invalid double '" + value + "'");
    private static final Para0CommandExceptionType READER_EXPECTED_DOUBLE = new Para0CommandExceptionType("Expected double");
    private static final DynamicCommandExceptionType READER_INVALID_FLOAT = new DynamicCommandExceptionType(value -> "Invalid float '" + value + "'");
    private static final Para0CommandExceptionType READER_EXPECTED_FLOAT = new Para0CommandExceptionType("Expected float");
    private static final Para0CommandExceptionType READER_EXPECTED_BOOL = new Para0CommandExceptionType("Expected bool");
    private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(symbol -> "Expected '" + symbol + "'");

    private static final Para0CommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new Para0CommandExceptionType("Unknown command");
    private static final Para0CommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new Para0CommandExceptionType("Incorrect argument for command");
    private static final Para0CommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new Para0CommandExceptionType("Expected whitespace to end one argument, but found trailing data");
    private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(message -> "Could not parse command: " + message);


    @Override
    public Dynamic2CommandExceptionType doubleTooLow() {
        return DOUBLE_TOO_LOW;
    }

    @Override
    public Dynamic2CommandExceptionType doubleTooHigh() {
        return DOUBLE_TOO_HIGH;
    }

    @Override
    public Dynamic2CommandExceptionType floatTooLow() {
        return FLOAT_TOO_LOW;
    }

    @Override
    public Dynamic2CommandExceptionType floatTooHigh() {
        return FLOAT_TOO_HIGH;
    }

    @Override
    public Dynamic2CommandExceptionType integerTooLow() {
        return INTEGER_TOO_LOW;
    }

    @Override
    public Dynamic2CommandExceptionType integerTooHigh() {
        return INTEGER_TOO_HIGH;
    }

    @Override
    public Dynamic2CommandExceptionType longTooLow() {
        return LONG_TOO_LOW;
    }

    @Override
    public Dynamic2CommandExceptionType longTooHigh() {
        return LONG_TOO_HIGH;
    }

    @Override
    public DynamicCommandExceptionType literalIncorrect() {
        return LITERAL_INCORRECT;
    }

    @Override
    public Para0CommandExceptionType readerExpectedStartOfQuote() {
        return READER_EXPECTED_START_OF_QUOTE;
    }

    @Override
    public Para0CommandExceptionType readerExpectedEndOfQuote() {
        return READER_EXPECTED_END_OF_QUOTE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidEscape() {
        return READER_INVALID_ESCAPE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidBool() {
        return READER_INVALID_BOOL;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidInt() {
        return READER_INVALID_INT;
    }

    @Override
    public Para0CommandExceptionType readerExpectedInt() {
        return READER_EXPECTED_INT;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidLong() {
        return READER_INVALID_LONG;
    }

    @Override
    public Para0CommandExceptionType readerExpectedLong() {
        return READER_EXPECTED_LONG;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidDouble() {
        return READER_INVALID_DOUBLE;
    }

    @Override
    public Para0CommandExceptionType readerExpectedDouble() {
        return READER_EXPECTED_DOUBLE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidFloat() {
        return READER_INVALID_FLOAT;
    }

    @Override
    public Para0CommandExceptionType readerExpectedFloat() {
        return READER_EXPECTED_FLOAT;
    }

    @Override
    public Para0CommandExceptionType readerExpectedBool() {
        return READER_EXPECTED_BOOL;
    }

    @Override
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return READER_EXPECTED_SYMBOL;
    }

    @Override
    public Para0CommandExceptionType dispatcherUnknownCommand() {
        return DISPATCHER_UNKNOWN_COMMAND;
    }

    @Override
    public Para0CommandExceptionType dispatcherUnknownArgument() {
        return DISPATCHER_UNKNOWN_ARGUMENT;
    }

    @Override
    public Para0CommandExceptionType dispatcherExpectedArgumentSeparator() {
        return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }
    @Override
    public DynamicCommandExceptionType dispatcherParseException() {
        return DISPATCHER_PARSE_EXCEPTION;
    }
}
