package net.cjsah.console.command;

import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

public class StringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';
    private final String string;
    private int cursor = 0;

    public StringReader(String string) {
        this.string = string;
    }

    public StringReader copy() {
        StringReader reader = new StringReader(this.string);
        reader.cursor = this.cursor;
        return reader;
    }

    public String getString() {
        return this.string;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    public int getRemainingLength() {
        return this.string.length() - cursor;
    }

    public int getTotalLength() {
        return this.string.length();
    }

    public int getCursor() {
        return this.cursor;
    }

    public String getRead() {
        return this.string.substring(0, cursor);
    }

    public String getRemaining() {
        return this.string.substring(cursor);
    }

    public boolean canRead(final int length) {
        return this.cursor + length <= this.string.length();
    }

    public boolean canRead() {
        return canRead(1);
    }

    public char peek() {
        return this.string.charAt(this.cursor);
    }

    public char peek(final int offset) {
        return this.string.charAt(this.cursor + offset);
    }

    public char read() {
        return this.string.charAt(this.cursor++);
    }

    public void skip() {
        this.cursor++;
    }

    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public int readInt() throws CommandException {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.readerExpectedInt().createWithContext(this);
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            this.cursor = start;
            throw BuiltExceptions.readerInvalidInt().createWithContext(this, number);
        }
    }

    public long readLong() throws CommandException {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.readerExpectedLong().createWithContext(this);
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            this.cursor = start;
            throw BuiltExceptions.readerInvalidLong().createWithContext(this, number);
        }
    }

    public double readDouble() throws CommandException {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.readerExpectedDouble().createWithContext(this);
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            this.cursor = start;
            throw BuiltExceptions.readerInvalidDouble().createWithContext(this, number);
        }
    }

    public float readFloat() throws CommandException {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw BuiltExceptions.readerExpectedFloat().createWithContext(this);
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            this.cursor = start;
            throw BuiltExceptions.readerInvalidFloat().createWithContext(this, number);
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = this.cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return this.string.substring(start, this.cursor);
    }

    public String readQuotedString() throws CommandException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw BuiltExceptions.readerExpectedStartOfQuote().createWithContext(this);
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw BuiltExceptions.readerInvalidEscape().createWithContext(this, String.valueOf(c));
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw BuiltExceptions.readerExpectedEndOfQuote().createWithContext(this);
    }

    public String readString() throws CommandException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws CommandException {
        final int start = this.cursor;
        final String value = readString();
        if (value.isEmpty()) {
            throw BuiltExceptions.readerExpectedBool().createWithContext(this);
        }

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            this.cursor = start;
            throw BuiltExceptions.readerInvalidBool().createWithContext(this, value);
        }
    }

    public void expect(final char c) throws CommandException {
        if (!canRead() || peek() != c) {
            throw BuiltExceptions.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
        }
        skip();
    }






}
