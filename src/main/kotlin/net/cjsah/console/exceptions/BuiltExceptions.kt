package net.cjsah.console.exceptions

object BuiltExceptions {
    @JvmField val DOUBLE_TOO_LOW = Para2CommandException { found: Any, min: Any -> "Double 必须小于 $min, 但是发现了 $found" }
    @JvmField val DOUBLE_TOO_HIGH = Para2CommandException { found: Any, max: Any -> "Double 必须大于 $max, 但是发现了 $found" }

    @JvmField val FLOAT_TOO_LOW = Para2CommandException { found: Any, min: Any -> "Float 必须小于 $min, 但是发现了 $found"}
    @JvmField val FLOAT_TOO_HIGH = Para2CommandException { found: Any, max: Any -> "Float 必须大于 $max, 但是发现了 $found"}

    @JvmField val INTEGER_TOO_LOW = Para2CommandException { found: Any, min: Any -> "Integer 必须小于 $min, 但是发现了 $found"}
    @JvmField val INTEGER_TOO_HIGH = Para2CommandException { found: Any, max: Any -> "Integer 必须大于 $max, 但是发现了 $found"}

    @JvmField val LONG_TOO_LOW = Para2CommandException { found: Any, min: Any -> "Long 必须小于 $min, 但是发现了 $found"}
    @JvmField val LONG_TOO_HIGH = Para2CommandException { found: Any, max: Any -> "Long 必须大于 $max, 但是发现了 $found"}

    @JvmField val LITERAL_INCORRECT = Para1CommandException { expected: Any -> "预期 $expected"}

    @JvmField val READER_EXPECTED_START_OF_QUOTE = Para0CommandException("字符串预期以引号开始")
    @JvmField val READER_EXPECTED_END_OF_QUOTE = Para0CommandException("字符串未以引号闭合")
    
    @JvmField val READER_INVALID_ESCAPE = Para1CommandException { character: Any -> "在字符串中的转义 '$character' 无效" }
    @JvmField val READER_INVALID_BOOL = Para1CommandException { value: Any -> "无效的 Bool, 预期为 'true' 或 'false' 但是发现了 '$value'" }
    @JvmField val READER_INVALID_INT = Para1CommandException { value: Any -> "无效的 integer '$value'" }
    @JvmField val READER_EXPECTED_INT = Para0CommandException("无效的 integer")
    @JvmField val READER_INVALID_LONG = Para1CommandException { value: Any ->  "无效的 long '$value'" }
    @JvmField val READER_EXPECTED_LONG = Para0CommandException("无效的 long")
    @JvmField val READER_INVALID_DOUBLE = Para1CommandException { value: Any -> "无效的 double '$value'" }
    @JvmField val READER_EXPECTED_DOUBLE = Para0CommandException("无效的 double")
    @JvmField val READER_INVALID_FLOAT = Para1CommandException { value: Any -> "无效的 float '$value'" }
    @JvmField val READER_EXPECTED_FLOAT = Para0CommandException("无效的 float")
    @JvmField val READER_EXPECTED_BOOL = Para0CommandException("无效的 bool")
    @JvmField val READER_EXPECTED_SYMBOL = Para1CommandException { symbol: Any -> "无效的 '$symbol'" }

    @JvmField val DISPATCHER_UNKNOWN_COMMAND = Para0CommandException("未知指令")
    @JvmField val DISPATCHER_UNKNOWN_ARGUMENT = Para0CommandException("参数错误 / 没有权限")
    @JvmField val DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = Para0CommandException("需要空格来结束一个参数，但发现尾随数据 ")
    @JvmField val DISPATCHER_PARSE_EXCEPTION = Para1CommandException { message: Any -> "无法解析命令: '$message'" }

    @JvmField val GROUP_NOT_FOUND = Para1CommandException { group: Any -> "机器人并没有加入此群组 '$group'" }
    @JvmField val FRIEND_NOT_FOUND = Para1CommandException { friend: Any -> "机器人并没有此好友 '$friend'" }
    @JvmField val PLUGIN_NOT_FOUND = Para1CommandException { plugin: Any -> "没有找到此插件: '$plugin'" }

}