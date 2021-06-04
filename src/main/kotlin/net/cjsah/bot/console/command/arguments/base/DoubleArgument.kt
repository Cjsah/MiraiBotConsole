@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.arguments.base

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException

class DoubleArgument private constructor(private val min: Double, private val max: Double) : Argument<Double> {

    companion object {
        fun float() = float(Double.MIN_VALUE)

        fun float(min: Double) = float(min, Double.MAX_VALUE)

        fun float(min: Double, max: Double) = DoubleArgument(min, max)

        fun getDouble(context: CommandContext, name: String) = context.getArgument(name, Double::class.java)
    }

    fun getMax() = max

    fun getMin() = min

    override fun parse(reader: StringReader): Double {
        val start = reader.getCursor()
        val result = reader.readDouble()
        if (result < min) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.doubleTooLow().createWithContext(reader, result, min)
        }
        if (result > max) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.doubleTooHigh().createWithContext(reader, result, max)
        }
        return result
    }

}