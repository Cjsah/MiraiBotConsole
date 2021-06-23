package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.source.CommandSource

class CommandContext(
    private val arguments: Map<String, ParsedNodeResult<*>>,
    val source: CommandSource<*>,
    val command: Command?,
) {

    @Suppress("UNCHECKED_CAST")
    fun <V> getArgument(name: String, clazz: Class<V>): V {
        val argument = arguments[name] ?: throw IllegalArgumentException("此命令中不存在参数 '$name'")
        val result = argument.result!!
        return if (ClassConversion.get(clazz).isAssignableFrom(result::class.java)) {
            result as V
        } else {
            throw IllegalArgumentException("参数 '" + name + "' 被定义为 " + result::class.java.simpleName + ", 而不是 " + clazz)
        }
    }
}