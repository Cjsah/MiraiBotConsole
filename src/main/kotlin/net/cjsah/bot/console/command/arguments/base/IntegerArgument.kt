@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.arguments.base

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException

class IntegerArgument private constructor(private val min: Int, private val max: Int) : Argument<Int> {

    companion object {
        fun integer() = integer(Int.MIN_VALUE)

        fun integer(min: Int) = integer(min, Int.MAX_VALUE)

        fun integer(min: Int, max: Int) = IntegerArgument(min, max)

        fun getInteger(context: CommandContext, name: String) = context.getArgument(name, Int::class.java)
    }

    fun getMax() = max

    fun getMin() = min

    override fun parse(reader: StringReader): Int {
        val start = reader.getCursor()
        val result = reader.readInt()
        if (result < min) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.integerTooLow().createWithContext(reader, result, min)
        }
        if (result > max) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, max)
        }
        return result
    }
}