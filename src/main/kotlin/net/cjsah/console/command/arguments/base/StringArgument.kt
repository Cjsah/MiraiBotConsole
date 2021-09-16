@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.console.command.arguments.base

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext

class StringArgument private constructor(private val space: Boolean) : Argument<String> {

    companion object {
        fun word() = StringArgument(false)

        fun string() = StringArgument(true)

        fun getString(context: CommandContext, name: String) = context.getArgument(name, String::class.java)
    }

    override fun parse(reader: StringReader): String {
        return if (space) {
            val text = reader.getRemaining()
            reader.setCursor(reader.getTotalLength())
            text
        }else {
            reader.readUnquotedString()
        }
    }
}