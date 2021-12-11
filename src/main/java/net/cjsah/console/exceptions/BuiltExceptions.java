package net.cjsah.console.exceptions;

@SuppressWarnings("unused")
public class BuiltExceptions {
    private static final Para2CommandException DOUBLE_TOO_LOW = new Para2CommandException((found, min) -> String.format("Double 必须小于 %s, 但是发现了 %s", min, found));
    private static final Para2CommandException DOUBLE_TOO_HIGH = new Para2CommandException((found, max) -> String.format("Double 必须大于 %s, 但是发现了 %s", max, found));

    private static final Para2CommandException FLOAT_TOO_LOW = new Para2CommandException((found, min) -> String.format("Float 必须小于 %s, 但是发现了 %s", min, found));
    private static final Para2CommandException FLOAT_TOO_HIGH = new Para2CommandException((found, max) -> String.format("Float 必须大于 %s, 但是发现了 %s", max, found));

    private static final Para2CommandException INTEGER_TOO_LOW = new Para2CommandException((found, min) -> String.format("Integer 必须小于 %s, 但是发现了 %s", min, found));
    private static final Para2CommandException INTEGER_TOO_HIGH = new Para2CommandException((found, max) -> String.format("Integer 必须大于 %s, 但是发现了 %s", max, found));

    private static final Para2CommandException LONG_TOO_LOW = new Para2CommandException((found, min) -> String.format("Long 必须小于 %s, 但是发现了 %s", min, found));
    private static final Para2CommandException LONG_TOO_HIGH = new Para2CommandException((found, max) -> String.format("Long 必须大于 %s, 但是发现了 %s", max, found));

    private static final Para1CommandException LITERAL_INCORRECT = new Para1CommandException((expected) -> String.format("预期 %s", expected));

    private static final Para0CommandException READER_EXPECTED_START_OF_QUOTE = new Para0CommandException("字符串预期以引号开始");
    private static final Para0CommandException READER_EXPECTED_END_OF_QUOTE = new Para0CommandException("字符串未以引号闭合");
    private static final Para1CommandException READER_INVALID_ESCAPE = new Para1CommandException((character) -> String.format("在字符串中的转义 '%s' 无效", character));
    private static final Para1CommandException READER_INVALID_BOOL = new Para1CommandException((value) -> String.format("无效的 Bool, 预期为 'true' 或 'false' 但是发现了 '%s'", value));
    private static final Para1CommandException READER_INVALID_INT = new Para1CommandException((value) -> String.format("无效的 integer '%s'", value));
    private static final Para0CommandException READER_EXPECTED_INT = new Para0CommandException("无效的 integer");
    private static final Para1CommandException READER_INVALID_LONG = new Para1CommandException((value) -> String.format("无效的 long '%s'", value));
    private static final Para0CommandException READER_EXPECTED_LONG = new Para0CommandException("无效的 long");
    private static final Para1CommandException READER_INVALID_DOUBLE = new Para1CommandException((value) -> String.format("无效的 double '%s'", value));
    private static final Para0CommandException READER_EXPECTED_DOUBLE = new Para0CommandException("无效的 double");
    private static final Para1CommandException READER_INVALID_FLOAT = new Para1CommandException((value) -> String.format("无效的 float '%s'", value));
    private static final Para0CommandException READER_EXPECTED_FLOAT = new Para0CommandException("无效的 float");
    private static final Para0CommandException READER_EXPECTED_BOOL = new Para0CommandException("无效的 bool");
    private static final Para1CommandException READER_EXPECTED_SYMBOL = new Para1CommandException((symbol) -> String.format("无效的 '%s'", symbol));

    private static final Para0CommandException DISPATCHER_UNKNOWN_COMMAND = new Para0CommandException("未知指令");
    private static final Para0CommandException DISPATCHER_UNKNOWN_ARGUMENT = new Para0CommandException("参数错误 / 没有权限");
    private static final Para0CommandException DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new Para0CommandException("需要空格来结束一个参数，但发现尾随数据 ");
    private static final Para1CommandException DISPATCHER_PARSE_EXCEPTION = new Para1CommandException((message) -> String.format("无法解析命令: '%s'", message));

    private static final Para1CommandException GROUP_NOT_FOUND = new Para1CommandException((group) -> String.format("机器人并没有加入此群组 '%s'", group));
    private static final Para1CommandException FRIEND_NOT_FOUND = new Para1CommandException((friend) -> String.format("机器人并没有此好友 '%s'", friend));
    private static final Para1CommandException PLUGIN_NOT_FOUND = new Para1CommandException((plugin) -> String.format("没有找到此插件: '%s'", plugin));

    public static Para2CommandException doubleTooLow() {
        return DOUBLE_TOO_LOW;
    }

    public static Para2CommandException doubleTooHigh() {
        return DOUBLE_TOO_HIGH;
    }

    public static Para2CommandException floatTooLow() {
        return FLOAT_TOO_LOW;
    }

    public static Para2CommandException floatTooHigh() {
        return FLOAT_TOO_HIGH;
    }

    public static Para2CommandException integerTooLow() {
        return INTEGER_TOO_LOW;
    }

    public static Para2CommandException integerTooHigh() {
        return INTEGER_TOO_HIGH;
    }

    public static Para2CommandException longTooLow() {
        return LONG_TOO_LOW;
    }

    public static Para2CommandException longTooHigh() {
        return LONG_TOO_HIGH;
    }

    public static Para1CommandException literalIncorrect() {
        return LITERAL_INCORRECT;
    }

    public static Para0CommandException readerExpectedStartOfQuote() {
        return READER_EXPECTED_START_OF_QUOTE;
    }

    public static Para0CommandException readerExpectedEndOfQuote() {
        return READER_EXPECTED_END_OF_QUOTE;
    }

    public static Para1CommandException readerInvalidEscape() {
        return READER_INVALID_ESCAPE;
    }

    public static Para1CommandException readerInvalidBool() {
        return READER_INVALID_BOOL;
    }

    public static Para1CommandException readerInvalidInt() {
        return READER_INVALID_INT;
    }

    public static Para0CommandException readerExpectedInt() {
        return READER_EXPECTED_INT;
    }

    public static Para1CommandException readerInvalidLong() {
        return READER_INVALID_LONG;
    }

    public static Para0CommandException readerExpectedLong() {
        return READER_EXPECTED_LONG;
    }

    public static Para1CommandException readerInvalidDouble() {
        return READER_INVALID_DOUBLE;
    }

    public static Para0CommandException readerExpectedDouble() {
        return READER_EXPECTED_DOUBLE;
    }

    public static Para1CommandException readerInvalidFloat() {
        return READER_INVALID_FLOAT;
    }

    public static Para0CommandException readerExpectedFloat() {
        return READER_EXPECTED_FLOAT;
    }

    public static Para0CommandException readerExpectedBool() {
        return READER_EXPECTED_BOOL;
    }

    public static Para1CommandException readerExpectedSymbol() {
        return READER_EXPECTED_SYMBOL;
    }

    public static Para0CommandException dispatcherUnknownCommand() {
        return DISPATCHER_UNKNOWN_COMMAND;
    }

    public static Para0CommandException dispatcherUnknownArgument() {
        return DISPATCHER_UNKNOWN_ARGUMENT;
    }

    public static Para0CommandException dispatcherExpectedArgumentSeparator() {
        return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }

    public static Para1CommandException dispatcherParseException() {
        return DISPATCHER_PARSE_EXCEPTION;
    }

    public static Para1CommandException groupNotFound() {
        return GROUP_NOT_FOUND;
    }

    public static Para1CommandException pluginNotFound() {
        return PLUGIN_NOT_FOUND;
    }

    public static Para1CommandException friendNotFound() {
        return FRIEND_NOT_FOUND;
    }
}
