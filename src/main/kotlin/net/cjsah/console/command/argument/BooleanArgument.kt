@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext

class BooleanArgument private constructor() : Argument<Boolean> {

    companion object {
        @JvmStatic
        fun bool() = BooleanArgument()

        @JvmStatic
        fun getBoolean(context: CommandContext, name: String) =  context.getArgument(name, Boolean::class.javaPrimitiveType!!)
    }

    override fun parse(reader: StringReader) = reader.readBoolean()
}