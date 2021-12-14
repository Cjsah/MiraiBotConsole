@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions

class LongArgument private constructor(
    private val min: Long,
    private val max: Long
) : Argument<Long> {

    companion object {
        @JvmStatic
        fun longArg() = longArg(Long.MIN_VALUE)

        @JvmStatic
        fun longArg(min: Long) = longArg(min, Long.MAX_VALUE)

        @JvmStatic
        fun longArg(min: Long, max: Long) = LongArgument(min, max)

        @JvmStatic
        fun getLong(context: CommandContext, name: String) = context.getArgument(name, Long::class.javaPrimitiveType!!)
    }

    fun getMin() = min

    fun getMax() = max

    override fun parse(reader: StringReader): Long {
        val start = reader.getCursor()
        val result = reader.readLong()
        if (result < min) {
            reader.setCursor(start)
            throw BuiltExceptions.LONG_TOO_LOW.createWithContext(reader, result, max)
        }
        if (result > max) {
            reader.setCursor(start)
            throw BuiltExceptions.LONG_TOO_HIGH.createWithContext(reader, result, max)
        }
        return result
    }
}