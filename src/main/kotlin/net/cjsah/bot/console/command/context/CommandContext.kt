package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.Command

class CommandContext(
    private val arguments: Map<String, ParsedNodeResult<*>>,
    private val command: Command?,
) {

    @Suppress("UNCHECKED_CAST")
    fun <V> getArgument(name: String, clazz: Class<V>): V {
        val argument = arguments[name] ?: throw IllegalArgumentException("No such argument '$name' exists on this command")
        val result = argument.result!!
        return if (ClassConversion.get(clazz).isAssignableFrom(result::class.java)) {
            result as V
        } else {
            throw IllegalArgumentException("Argument '" + name + "' is defined as " + result::class.java.simpleName + ", not " + clazz)
        }
    }

    fun getCommand() = command
}