package net.cjsah.bot.console.command.exceptions

import net.cjsah.bot.console.command.StringReaderProvider

class Para2CommandException(private val function: Function) : CommandExceptionType {

    fun create(a: Any, b: Any): CommandException {
        return CommandException(this, function.apply(a, b))
    }

    fun createWithContext(reader: StringReaderProvider, a: Any, b: Any): CommandException {
        return CommandException(this, function.apply(a, b), reader.getString(), reader.getCursor())
    }

    fun interface Function {
        fun apply(a: Any, b: Any): String
    }

}