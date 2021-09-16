@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.console.command.arguments.base

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.CommandException

class FloatArgument private constructor(private val min: Float, private val max: Float) : Argument<Float> {

    companion object {
        fun float() = float(Float.MIN_VALUE)

        fun float(min: Float) = float(min, Float.MAX_VALUE)

        fun float(min: Float, max: Float) = FloatArgument(min, max)

        fun getFloat(context: CommandContext, name: String) = context.getArgument(name, Float::class.java)
    }

    fun getMax() = max

    fun getMin() = min

    override fun parse(reader: StringReader): Float {
        val start = reader.getCursor()
        val result = reader.readFloat()
        if (result < min) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.floatTooLow().createWithContext(reader, result, min)
        }
        if (result > max) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.floatTooHigh().createWithContext(reader, result, max)
        }
        return result
    }
}