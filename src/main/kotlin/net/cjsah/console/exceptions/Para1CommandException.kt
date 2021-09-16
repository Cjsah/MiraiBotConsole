package net.cjsah.console.exceptions

import net.cjsah.console.command.StringReaderProvider
import java.util.function.Function

class Para1CommandException(private val function: Function<Any, String>) : CommandExceptionType {
    fun create(arg: Any): CommandException {
        return CommandException(this, function.apply(arg))
    }

    fun createWithContext(reader: StringReaderProvider, arg: Any): CommandException {
        return CommandException(this, function.apply(arg), reader.getString(), reader.getCursor())
    }
}