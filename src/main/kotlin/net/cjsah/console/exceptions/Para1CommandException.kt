@file:Suppress("unused")

package net.cjsah.console.exceptions

import net.cjsah.console.command.StringReader
import java.util.function.Function

class Para1CommandException(private val function: Function<Any, String>) : CommandExceptionType {

    fun create(arg: Any) = CommandException(this, function.apply(arg))

    fun createWithContext(reader: StringReader, arg: Any) = CommandException(this, function.apply(arg), reader.getString(), reader.getCursor())

}