package net.cjsah.console.command.context

class Range {
    val start: Int
    val end: Int

    constructor(pos: Int) : this(pos, pos)

    constructor(start: Int, end: Int) {
        this.start = start
        this.end = end
    }

    constructor(a: Range, b: Range) : this(a.start.coerceAtMost(b.start), a.end.coerceAtLeast(b.end))

    fun isEmpty() = start == end

}