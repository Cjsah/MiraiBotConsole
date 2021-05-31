package net.cjsah.bot.console.command.argument

interface ArgumentType {

    fun isThisCommand(content: String): Boolean

    fun splitSpace(): Boolean = true
}