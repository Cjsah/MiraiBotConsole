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

        private val GROUP_NOT_FOUND = Para1CommandException { group -> "The group $group was not found in this bot"}
        private val FRIEND_NOT_FOUND = Para1CommandException { friend -> "The friend $friend was not found in this bot"}
    }

    fun doubleTooLow() = DOUBLE_TOO_LOW

    fun doubleTooHigh() = DOUBLE_TOO_HIGH

    fun floatTooLow() = FLOAT_TOO_LOW

    fun floatTooHigh() = FLOAT_TOO_HIGH

    fun integerTooLow() = INTEGER_TOO_LOW

    fun integerTooHigh() = INTEGER_TOO_HIGH

    fun longTooLow() = LONG_TOO_LOW

    fun longTooHigh() = LONG_TOO_HIGH

    fun literalIncorrect() = LITERAL_INCORRECT

    fun readerExpectedStartOfQuote() = READER_EXPECTED_START_OF_QUOTE

    fun readerExpectedEndOfQuote() = READER_EXPECTED_END_OF_QUOTE

    fun readerInvalidEscape() = READER_INVALID_ESCAPE

    fun readerInvalidBool() = READER_INVALID_BOOL

    fun readerInvalidInt() = READER_INVALID_INT

    fun readerExpectedInt() = READER_EXPECTED_INT

    fun readerInvalidLong() = READER_INVALID_LONG

    fun readerExpectedLong() = READER_EXPECTED_LONG

    fun readerInvalidDouble() = READER_INVALID_DOUBLE

    fun readerExpectedDouble() = READER_EXPECTED_DOUBLE

    fun readerInvalidFloat() = READER_INVALID_FLOAT

    fun readerExpectedFloat() = READER_EXPECTED_FLOAT

    fun readerExpectedBool() = READER_EXPECTED_BOOL

    fun readerExpectedSymbol() = READER_EXPECTED_SYMBOL

    fun dispatcherUnknownCommand() = DISPATCHER_UNKNOWN_COMMAND

    fun dispatcherUnknownArgument() = DISPATCHER_UNKNOWN_ARGUMENT

    fun dispatcherExpectedArgumentSeparator() = DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR

    fun dispatcherParseException() = DISPATCHER_PARSE_EXCEPTION

    fun groupNotFound() = GROUP_NOT_FOUND

    fun friendNotFound() = FRIEND_NOT_FOUND

}