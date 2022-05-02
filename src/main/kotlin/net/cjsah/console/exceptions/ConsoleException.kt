package net.cjsah.console.exceptions

import net.cjsah.console.text.Text

object ConsoleException {
    fun <T> create(text: Text, exception: Class<T>): T =
        exception.getConstructor(String::class.java).newInstance(text.toString())
}