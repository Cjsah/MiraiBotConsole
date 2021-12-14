@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext

class StringArgument private constructor(private val space: Boolean): Argument<String> {

    companion object {
        @JvmStatic
        fun word() = StringArgument(false)

        @JvmStatic
        fun string() = StringArgument(true)

        @JvmStatic
        fun getString(context: CommandContext, name: String) = context.getArgument(name, String::class.java)
    }

    override fun parse(reader: StringReader): String {
        return if (space) reader.getRemaining().apply { reader.setCursor(reader.getTotalLength()) }
        else reader.readUnquotedString()
    }

}