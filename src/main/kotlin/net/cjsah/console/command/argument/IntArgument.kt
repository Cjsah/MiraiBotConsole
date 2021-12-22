@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions

class IntArgument private constructor(
    private val min: Int,
    private val max: Int
) : Argument<Int> {

    companion object {
        @JvmStatic
        fun integer() = integer(Int.MIN_VALUE)

        @JvmStatic
        fun integer(min: Int) = integer(min, Int.MAX_VALUE)

        @JvmStatic
        fun integer(min: Int, max: Int) = IntArgument(min, max)

        @JvmStatic
        fun getInteger(context: CommandContext, name: String) = context.getArgument(name, Int::class.javaPrimitiveType!!)
    }

    fun getMin() = min

    fun getMax() = max

    override fun parse(reader: StringReader): Int {
        val start = reader.getCursor()
        val result = reader.readInt()
        if (result < min) {
            reader.setCursor(start)
            throw BuiltExceptions.INTEGER_TOO_LOW.createWithContext(reader, result, max)
        }
        if (result > max) {
            reader.setCursor(start)
            throw BuiltExceptions.INTEGER_TOO_HIGH.createWithContext(reader, result, max)
        }
        return result
    }
}