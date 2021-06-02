@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.exceptions.CommandException

class StringReader(private val string: String) : ImmutableStringReader {
    private val SYNTAX_ESCAPE = '\\'
    private val SYNTAX_DOUBLE_QUOTE = '"'
    private val SYNTAX_SINGLE_QUOTE = '\''

    private var cursor: Int = 0
    override fun getString(): String {
        return string
    }

    fun setCursor(cursor: Int) {
        this.cursor = cursor
    }

    override fun getRemainingLength(): Int {
        return string.length - cursor
    }

    override fun getTotalLength(): Int {
        return string.length
    }

    override fun getCursor(): Int {
        return cursor
    }

    override fun getRead(): String {
        return string.substring(0, cursor)
    }

    override fun getRemaining(): String {
        return string.substring(cursor)
    }

    override fun canRead(length: Int): Boolean {
        return cursor + length <= string.length
    }

    override fun canRead(): Boolean {
        return canRead(1)
    }

    override fun peek(): Char {
        return string.toCharArray()[cursor]
    }

    override fun peek(offset: Int): Char {
        return string.toCharArray()[cursor + offset]
    }

    fun read(): Char {
        return string[cursor++]
    }

    fun skip() {
        cursor++
    }
    fun isAllowedNumber(c: Char): Boolean {
        return c in '0'..'9' || c == '.' || c == '-'
    }

    fun isQuotedStringStart(c: Char): Boolean {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE
    }

    fun skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip()
        }
    }

    @Throws(CommandException::class)
    fun readInt(): Int {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this)
        }
        return try {
            number.toInt()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number)
        }
    }

    @Throws(CommandException::class)
    fun readLong(): Long {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this)
        }
        return try {
            number.toLong()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number)
        }
    }

    @Throws(CommandException::class)
    fun readDouble(): Double {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this)
        }
        return try {
            number.toDouble()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number)
        }
    }

    @Throws(CommandException::class)
    fun readFloat(): Float {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this)
        }
        return try {
            number.toFloat()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number)
        }
    }

    fun isAllowedInUnquotedString(c: Char): Boolean {
        return c in '0'..'9' || c in 'A'..'Z' || c in 'a'..'z' || c == '_' || c == '-' || c == '.' || c == '+'
    }

    fun readUnquotedString(): String {
        val start = cursor
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip()
        }
        return string.substring(start, cursor)
    }

    @Throws(CommandException::class)
    fun readQuotedString(): String {
        if (!canRead()) {
            return ""
        }
        val next = peek()
        if (!isQuotedStringStart(next)) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this)
        }
        skip()
        return readStringUntil(next)
    }

    @Throws(CommandException::class)
    fun readStringUntil(terminator: Char): String {
        val result = StringBuilder()
        var escaped = false
        while (canRead()) {
            val c = read()
            when {
                escaped -> {
                    escaped = if (c == terminator || c == SYNTAX_ESCAPE) {
                        result.append(c)
                        false
                    } else {
                        setCursor(getCursor() - 1)
                        throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidEscape()
                            .createWithContext(this, c.toString())
                    }
                }
                c == SYNTAX_ESCAPE -> escaped = true
                c == terminator -> return result.toString()
                else -> result.append(c)
            }
        }
        throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this)
    }

    @Throws(CommandException::class)
    fun readString(): String {
        if (!canRead()) {
            return ""
        }
        val next = peek()
        if (isQuotedStringStart(next)) {
            skip()
            return readStringUntil(next)
        }
        return readUnquotedString()
    }

    @Throws(CommandException::class)
    fun readBoolean(): Boolean {
        val start = cursor
        val value = readString()
        if (value.isEmpty()) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this)
        }
        return when (value) {
            "true" -> true
            "false" -> false
            else -> {
                cursor = start
                throw CommandException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value)
            }
        }
    }

    @Throws(CommandException::class)
    fun expect(c: Char) {
        if (!canRead() || peek() != c) {
            throw CommandException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, c.toString())
        }
        skip()
    }

}