package net.cjsah.console.command

interface StringReaderProvider {
    fun getString(): String

    fun getRemainingLength(): Int

    fun getTotalLength(): Int

    fun getCursor(): Int

    fun getRead(): String

    fun getRemaining(): String

    fun canRead(length: Int): Boolean

    fun canRead(): Boolean

    fun peek(): Char

    fun peek(offset: Int): Char

}