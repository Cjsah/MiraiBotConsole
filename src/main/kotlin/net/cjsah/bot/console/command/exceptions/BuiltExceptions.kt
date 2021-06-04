package net.cjsah.bot.console.command.exceptions

class BuiltExceptions {
    companion object {
        private val DOUBLE_TOO_LOW = Para2CommandException { found, min -> "Double must not be less than $min, found $found" }
        private val DOUBLE_TOO_HIGH = Para2CommandException { found, max -> "Double must not be more than $max, found $found" }

        private val FLOAT_TOO_LOW = Para2CommandException { found, min -> "Float must not be less than $min, found $found" }
        private val FLOAT_TOO_HIGH = Para2CommandException { found, max -> "Float must not be more than $max, found $found" }

        private val INTEGER_TOO_LOW = Para2CommandException { found, min -> "Integer must not be less than $min, found $found" }
        private val INTEGER_TOO_HIGH = Para2CommandException { found, max -> "Integer must not be more than $max, found $found" }

        private val LONG_TOO_LOW = Para2CommandException { found, min -> "Long must not be less than $min, found $found" }
        private val LONG_TOO_HIGH = Para2CommandException { found, max -> "Long must not be more than $max, found $found" }

        private val LITERAL_INCORRECT = Para1CommandException { expected -> "Expected literal $expected" }

        private val READER_EXPECTED_START_OF_QUOTE = Para0CommandException("Expected quote to start a string")
        private val READER_EXPECTED_END_OF_QUOTE = Para0CommandException("Unclosed quoted string")
        private val READER_INVALID_ESCAPE = Para1CommandException { character -> "Invalid escape sequence '$character' in quoted string" }
        private val READER_INVALID_BOOL = Para1CommandException { value -> "Invalid bool, expected true or false but found '$value'" }
        private val READER_INVALID_INT = Para1CommandException { value -> "Invalid integer '$value'" }
        private val READER_EXPECTED_INT = Para0CommandException("Expected integer")
        private val READER_INVALID_LONG = Para1CommandException { value -> "Invalid long '$value'" }
        private val READER_EXPECTED_LONG = Para0CommandException("Expected long")
        private val READER_INVALID_DOUBLE = Para1CommandException { value -> "Invalid double '$value'" }
        private val READER_EXPECTED_DOUBLE = Para0CommandException("Expected double")
        private val READER_INVALID_FLOAT = Para1CommandException { value -> "Invalid float '$value'" }
        private val READER_EXPECTED_FLOAT = Para0CommandException("Expected float")
        private val READER_EXPECTED_BOOL = Para0CommandException("Expected bool")
        private val READER_EXPECTED_SYMBOL = Para1CommandException { symbol -> "Expected '$symbol'" }

        private val DISPATCHER_UNKNOWN_COMMAND = Para0CommandException("Unknown Command")
        private val DISPATCHER_UNKNOWN_ARGUMENT = Para0CommandException("Incorrect argument for command")
        private val DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = Para0CommandException("Expected whitespace to end one argument, but found trailing data")
        private val DISPATCHER_PARSE_EXCEPTION = Para1CommandException { message -> "Could not parse command: $message" }
    }

    fun doubleTooLow(): Para2CommandException {
        return DOUBLE_TOO_LOW
    }

    fun doubleTooHigh(): Para2CommandException {
        return DOUBLE_TOO_HIGH
    }

    fun floatTooLow(): Para2CommandException {
        return FLOAT_TOO_LOW
    }

    fun floatTooHigh(): Para2CommandException {
        return FLOAT_TOO_HIGH
    }

    fun integerTooLow(): Para2CommandException {
        return INTEGER_TOO_LOW
    }

    fun integerTooHigh(): Para2CommandException {
        return INTEGER_TOO_HIGH
    }

    fun longTooLow(): Para2CommandException {
        return LONG_TOO_LOW
    }

    fun longTooHigh(): Para2CommandException {
        return LONG_TOO_HIGH
    }

    fun literalIncorrect(): Para1CommandException {
        return LITERAL_INCORRECT
    }

    fun readerExpectedStartOfQuote(): Para0CommandException {
        return READER_EXPECTED_START_OF_QUOTE
    }

    fun readerExpectedEndOfQuote(): Para0CommandException {
        return READER_EXPECTED_END_OF_QUOTE
    }

    fun readerInvalidEscape(): Para1CommandException {
        return READER_INVALID_ESCAPE
    }

    fun readerInvalidBool(): Para1CommandException {
        return READER_INVALID_BOOL
    }

    fun readerInvalidInt(): Para1CommandException {
        return READER_INVALID_INT
    }

    fun readerExpectedInt(): Para0CommandException {
        return READER_EXPECTED_INT
    }

    fun readerInvalidLong(): Para1CommandException {
        return READER_INVALID_LONG
    }

    fun readerExpectedLong(): Para0CommandException {
        return READER_EXPECTED_LONG
    }

    fun readerInvalidDouble(): Para1CommandException {
        return READER_INVALID_DOUBLE
    }

    fun readerExpectedDouble(): Para0CommandException {
        return READER_EXPECTED_DOUBLE
    }

    fun readerInvalidFloat(): Para1CommandException {
        return READER_INVALID_FLOAT
    }

    fun readerExpectedFloat(): Para0CommandException {
        return READER_EXPECTED_FLOAT
    }

    fun readerExpectedBool(): Para0CommandException {
        return READER_EXPECTED_BOOL
    }

    fun readerExpectedSymbol(): Para1CommandException {
        return READER_EXPECTED_SYMBOL
    }

    fun dispatcherUnknownCommand(): Para0CommandException {
        return DISPATCHER_UNKNOWN_COMMAND
    }

    fun dispatcherUnknownArgument(): Para0CommandException {
        return DISPATCHER_UNKNOWN_ARGUMENT
    }

    fun dispatcherExpectedArgumentSeparator(): Para0CommandException {
        return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR
    }

    fun dispatcherParseException(): Para1CommandException {
        return DISPATCHER_PARSE_EXCEPTION
    }

}