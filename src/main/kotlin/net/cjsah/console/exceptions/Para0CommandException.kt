@file:Suppress("unused")

package net.cjsah.console.exceptions

import net.cjsah.console.command.StringReader

class Para0CommandException(private val message: String) : CommandExceptionType {

    fun create() = CommandException(this, message)

    fun createWithContext(reader: StringReader) = CommandException(this, message, reader.getString(), reader.getCursor())

    override fun toString() = message

}