package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.StringReader

class ParsedNodeResult<T>(private val start: Int, private val end: Int, val result: T) {
    fun getNode(reader: StringReader): String {
        return reader.getString().substring(start, end)
    }

    fun getRange(): IntRange {
        return IntRange(start, end)
    }

    fun isEmpty(): Boolean {
        return start == end
    }

    fun getLength(): Int {
        return end - start
    }
}