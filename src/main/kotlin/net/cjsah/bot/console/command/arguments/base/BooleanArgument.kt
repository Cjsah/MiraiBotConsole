@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.arguments.base

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.context.CommandContext

class BooleanArgument private constructor(): Argument<Boolean> {

    companion object {
        fun boolean() = BooleanArgument()

        fun getBoolean(context: CommandContext, name: String) = context.getArgument(name, Boolean::class.java)
    }

    override fun parse(reader: StringReader) = reader.readBoolean()
}