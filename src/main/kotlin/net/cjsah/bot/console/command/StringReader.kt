@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.exceptions.CommandException

class StringReader(private val string: String) : StringReaderProvider {
    private val SYNTAX_ESCAPE = '\\'
    private val SYNTAX_DOUBLE_QUOTE = '"'
    private val SYNTAX_SINGLE_QUOTE = '\''

    constructor(reader: StringReader) : this(reader.string) {
        this.cursor = reader.cursor
    }

    private var cursor: Int = 0

    fun read() = string[cursor++]

    fun isAllowedNumber(c: Char) = c in '0'..'9' || c == '.' || c == '-'

    fun isQuotedStringStart(c: Char) = c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE

    fun setCursor(cursor: Int) {
        this.cursor = cursor
    }

    fun skip() {
        cursor++
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedInt().createWithContext(this)
        }
        return try {
            number.toInt()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_EXCEPTIONS.readerInvalidInt().createWithContext(this, number)
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedLong().createWithContext(this)
        }
        return try {
            number.toLong()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_EXCEPTIONS.readerInvalidLong().createWithContext(this, number)
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedDouble().createWithContext(this)
        }
        return try {
            number.toDouble()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number)
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedFloat().createWithContext(this)
        }
        return try {
            number.toFloat()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw CommandException.BUILT_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number)
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this)
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
                        throw CommandException.BUILT_EXCEPTIONS.readerInvalidEscape()
                            .createWithContext(this, c.toString())
                    }
                }
                c == SYNTAX_ESCAPE -> escaped = true
                c == terminator -> return result.toString()
                else -> result.append(c)
            }
        }
        throw CommandException.BUILT_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this)
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
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedBool().createWithContext(this)
        }
        return when (value) {
            "true" -> true
            "false" -> false
            else -> {
                cursor = start
                throw CommandException.BUILT_EXCEPTIONS.readerInvalidBool().createWithContext(this, value)
            }
        }
    }

    @Throws(CommandException::class)
    fun expect(c: Char) {
        if (!canRead() || peek() != c) {
            throw CommandException.BUILT_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, c.toString())
        }
        skip()
    }

    override fun getString() = string

    override fun getRemainingLength() = string.length - cursor

    override fun getTotalLength() = string.length

    override fun getCursor() = cursor

    override fun getRead() = string.substring(0, cursor)

    override fun getRemaining() = string.substring(cursor)

    override fun canRead(length: Int) = cursor + length <= string.length

    override fun canRead() = canRead(1)

    override fun peek() = string.toCharArray()[cursor]

    override fun peek(offset: Int) = string.toCharArray()[cursor + offset]
}