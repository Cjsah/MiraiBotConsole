package net.cjsah.console.exceptions

class BuiltExceptions {
    companion object {
        private val DOUBLE_TOO_LOW = Para2CommandException { found, min -> "Double 必须小于 $min, 但是发现了 $found" }
        private val DOUBLE_TOO_HIGH = Para2CommandException { found, max -> "Double 必须大于 $max, 但是发现了 $found" }

        private val FLOAT_TOO_LOW = Para2CommandException { found, min -> "Float 必须小于 $min, 但是发现了 $found" }
        private val FLOAT_TOO_HIGH = Para2CommandException { found, max -> "Float 必须大于 $max, 但是发现了 $found" }

        private val INTEGER_TOO_LOW = Para2CommandException { found, min -> "Integer 必须小于 $min, 但是发现了 $found" }
        private val INTEGER_TOO_HIGH = Para2CommandException { found, max -> "Integer 必须大于 $max, 但是发现了 $found" }

        private val LONG_TOO_LOW = Para2CommandException { found, min -> "Long 必须小于 $min, 但是发现了 $found" }
        private val LONG_TOO_HIGH = Para2CommandException { found, max -> "Long 必须大于 $max, 但是发现了 $found" }

        private val LITERAL_INCORRECT = Para1CommandException { expected -> "预期 $expected" }

        private val READER_EXPECTED_START_OF_QUOTE = Para0CommandException("字符串预期以引号开始")
        private val READER_EXPECTED_END_OF_QUOTE = Para0CommandException("字符串未以引号闭合")
        private val READER_INVALID_ESCAPE = Para1CommandException { character -> "在字符串中的转义 '$character' 无效" }
        private val READER_INVALID_BOOL = Para1CommandException { value -> "无效的 Bool, 预期为 'true' 或 'false' 但是发现了 '$value'" }
        private val READER_INVALID_INT = Para1CommandException { value -> "无效的 integer '$value'" }
        private val READER_EXPECTED_INT = Para0CommandException("无效的 integer")
        private val READER_INVALID_LONG = Para1CommandException { value -> "无效的 long '$value'" }
        private val READER_EXPECTED_LONG = Para0CommandException("无效的 long")
        private val READER_INVALID_DOUBLE = Para1CommandException { value -> "无效的 double '$value'" }
        private val READER_EXPECTED_DOUBLE = Para0CommandException("无效的 double")
        private val READER_INVALID_FLOAT = Para1CommandException { value -> "无效的 float '$value'" }
        private val READER_EXPECTED_FLOAT = Para0CommandException("无效的 float")
        private val READER_EXPECTED_BOOL = Para0CommandException("无效的 bool")
        private val READER_EXPECTED_SYMBOL = Para1CommandException { symbol -> "无效的 '$symbol'" }

        private val DISPATCHER_UNKNOWN_COMMAND = Para0CommandException("未知指令")
        private val DISPATCHER_UNKNOWN_ARGUMENT = Para0CommandException("参数错误 / 没有权限")
        private val DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = Para0CommandException("需要空格来结束一个参数，但发现尾随数据 ")
        private val DISPATCHER_PARSE_EXCEPTION = Para1CommandException { message -> "无法解析命令: '$message'" }

        private val GROUP_NOT_FOUND = Para1CommandException { group -> "机器人并没有加入此群组 '$group'"}
        private val FRIEND_NOT_FOUND = Para1CommandException { friend -> "机器人并没有此好友 '$friend'"}
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