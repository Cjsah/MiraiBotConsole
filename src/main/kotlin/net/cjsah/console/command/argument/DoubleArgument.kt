@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions

class DoubleArgument private constructor(
    private val min: Double,
    private val max: Double
) : Argument<Double> {

    companion object {
        @JvmStatic
        fun doubleArg() = doubleArg(Double.MIN_VALUE)

        @JvmStatic
        fun doubleArg(min: Double) = doubleArg(min, Double.MAX_VALUE)

        @JvmStatic
        fun doubleArg(min: Double, max: Double) = DoubleArgument(min, max)

        @JvmStatic
        fun getDouble(context: CommandContext, name: String) = context.getArgument(name, Double::class.javaPrimitiveType!!)
    }

    fun getMin() = min

    fun getMax() = max

    override fun parse(reader: StringReader): Double {
        val start = reader.getCursor()
        val result = reader.readDouble()
        if (result < min) {
            reader.setCursor(start)
            throw BuiltExceptions.doubleTooLow().createWithContext(reader, result, max)
        }
        if (result > max) {
            reader.setCursor(start)
            throw BuiltExceptions.doubleTooHigh().createWithContext(reader, result, max)
        }
        return result
    }
}