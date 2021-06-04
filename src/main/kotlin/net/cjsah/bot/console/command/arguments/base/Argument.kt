package net.cjsah.bot.console.command.arguments.base

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.exceptions.CommandException

interface Argument<T> {
    @Throws(CommandException::class)
    fun parse(reader: StringReader): T
}