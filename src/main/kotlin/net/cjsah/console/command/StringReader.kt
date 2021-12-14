@file:Suppress("PrivatePropertyName", "unused", "MemberVisibilityCanBePrivate")

package net.cjsah.console.command

import net.cjsah.console.exceptions.BuiltExceptions
import net.cjsah.console.exceptions.CommandException

class StringReader(private val string: String) {
    private val SYNTAX_ESCAPE = '\\'
    private val SYNTAX_DOUBLE_QUOTE = '"'
    private val SYNTAX_SINGLE_QUOTE = '\''
    private var cursor = 0


    fun copy(): StringReader {
        val reader = StringReader(string)
        reader.cursor = cursor
        return reader
    }

    fun getString(): String {
        return string
    }

    fun setCursor(cursor: Int) {
        this.cursor = cursor
    }

    fun getRemainingLength(): Int {
        return string.length - cursor
    }

    fun getTotalLength(): Int {
        return string.length
    }

    fun getCursor(): Int {
        return cursor
    }

    fun getRead(): String {
        return string.substring(0, cursor)
    }

    fun getRemaining(): String {
        return string.substring(cursor)
    }

    fun canRead(length: Int): Boolean {
        return cursor + length <= string.length
    }

    fun canRead(): Boolean {
        return canRead(1)
    }

    fun peek(): Char {
        return string[cursor]
    }

    fun peek(offset: Int): Char {
        return string[cursor + offset]
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
            throw BuiltExceptions.readerExpectedInt().createWithContext(this)
        }
        return try {
            number.toInt()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltExceptions.readerInvalidInt().createWithContext(this, number)
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
            throw BuiltExceptions.readerExpectedLong().createWithContext(this)
        }
        return try {
            number.toLong()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltExceptions.readerInvalidLong().createWithContext(this, number)
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
            throw BuiltExceptions.readerExpectedDouble().createWithContext(this)
        }
        return try {
            number.toDouble()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltExceptions.readerInvalidDouble().createWithContext(this, number)
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
            throw BuiltExceptions.readerExpectedFloat().createWithContext(this)
        }
        return try {
            number.toFloat()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltExceptions.readerInvalidFloat().createWithContext(this, number)
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
            throw BuiltExceptions.readerExpectedStartOfQuote().createWithContext(this)
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
            if (escaped) {
                escaped = if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c)
                    false
                } else {
                    setCursor(getCursor() - 1)
                    throw BuiltExceptions.readerInvalidEscape().createWithContext(this, c.toString())
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true
            } else if (c == terminator) {
                return result.toString()
            } else {
                result.append(c)
            }
        }
        throw BuiltExceptions.readerExpectedEndOfQuote().createWithContext(this)
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
        if (value.isEmpty()) throw BuiltExceptions.readerExpectedBool().createWithContext(this)
        return when (value) {
            "true" -> true
            "false" -> false
            else -> {
                cursor = start
                throw BuiltExceptions.readerInvalidBool().createWithContext(this, value)
            }
        }
    }

    @Throws(CommandException::class)
    fun expect(c: Char) {
        if (!canRead() || peek() != c) throw BuiltExceptions.readerExpectedSymbol().createWithContext(this, c.toString())
        skip()
    }

}