@file:Suppress("unused")

package net.cjsah.console.exceptions

import net.cjsah.console.command.StringReader

class Para2CommandException(private val function: Function) : CommandExceptionType {

    fun create(a: Any, b: Any) = CommandException(this, function.apply(a, b))

    fun createWithContext(reader: StringReader, a: Any, b: Any) = CommandException(this, function.apply(a, b), reader.getString(), reader.getCursor())

    fun interface Function {
        fun apply(a: Any, b: Any): String
    }
}