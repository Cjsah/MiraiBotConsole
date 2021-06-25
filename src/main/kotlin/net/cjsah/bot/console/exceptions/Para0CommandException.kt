package net.cjsah.bot.console.exceptions

import net.cjsah.bot.console.command.StringReaderProvider

class Para0CommandException(private val message: String) : CommandExceptionType {

    fun create(): CommandException {
        return CommandException(this, message)
    }

    fun createWithContext(reader: StringReaderProvider): CommandException {
        return CommandException(this, message, reader.getString(), reader.getCursor())
    }

    override fun toString(): String {
        return message
    }

}