@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions

class FloatArgument private constructor(
    private val min: Float,
    private val max: Float
) : Argument<Float> {

    companion object {
        @JvmStatic
        fun floatArg() = floatArg(Float.MIN_VALUE)

        @JvmStatic
        fun floatArg(min: Float) = floatArg(min, Float.MAX_VALUE)

        @JvmStatic
        fun floatArg(min: Float, max: Float) = FloatArgument(min, max)

        @JvmStatic
        fun getFloat(context: CommandContext, name: String) = context.getArgument(name, Float::class.javaPrimitiveType!!)
    }

    fun getMin() = min

    fun getMax() = max

    override fun parse(reader: StringReader): Float {
        val start = reader.getCursor()
        val result = reader.readFloat()
        if (result < min) {
            reader.setCursor(start)
            throw BuiltExceptions.floatTooLow().createWithContext(reader, result, max)
        }
        if (result > max) {
            reader.setCursor(start)
            throw BuiltExceptions.floatTooHigh().createWithContext(reader, result, max)
        }
        return result
    }
}