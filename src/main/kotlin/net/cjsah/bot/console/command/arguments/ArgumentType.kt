package net.cjsah.bot.console.command.arguments

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.exceptions.CommandException

interface ArgumentType<T> {
    @Throws(CommandException::class)
    fun parse(reader: StringReader?): T
}