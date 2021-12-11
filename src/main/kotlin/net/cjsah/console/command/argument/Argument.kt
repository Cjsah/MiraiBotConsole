package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.exceptions.CommandException

interface Argument<T> {
    @Throws(CommandException::class)
    fun parse(reader: StringReader): T
}