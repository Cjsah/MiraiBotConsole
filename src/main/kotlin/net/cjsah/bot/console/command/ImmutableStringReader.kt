package net.cjsah.bot.console.command

interface ImmutableStringReader {
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