@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.bot.console.command.arguments.base

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException

class LongArgument private constructor(private val min: Long, private val max: Long) : Argument<Long> {

    companion object {
        fun long() = long(Long.MIN_VALUE)

        fun long(min: Long) = long(min, Long.MAX_VALUE)

        fun long(min: Long, max: Long) = LongArgument(min, max)

        fun getLong(context: CommandContext, name: String) = context.getArgument(name, Long::class.java)
    }

    fun getMax() = max

    fun getMin() = min

    override fun parse(reader: StringReader): Long {
        val start = reader.getCursor()
        val result = reader.readLong()
        if (result < min) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.longTooLow().createWithContext(reader, result, min)
        }
        if (result > max) {
            reader.setCursor(start)
            throw CommandException.BUILT_EXCEPTIONS.longTooHigh().createWithContext(reader, result, max)
        }
        return result
    }
}