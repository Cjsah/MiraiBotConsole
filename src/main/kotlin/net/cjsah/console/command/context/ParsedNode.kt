package net.cjsah.console.command.context

class ParsedNode<T>(
    private val start: Int,
    private val end: Int,
    private val result: T,
) {
    fun getRange(): Range {
        return Range(start, end)
    }

    fun getResult(): T {
        return result
    }
}