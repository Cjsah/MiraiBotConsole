package net.cjsah.console.command.context

class ParsedNodeResult<T>(private val start: Int, private val end: Int, val result: T) {
    fun getRange(): IntRange {
        return IntRange(start, end)
    }
}