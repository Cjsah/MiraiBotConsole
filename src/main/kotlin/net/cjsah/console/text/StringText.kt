package net.cjsah.console.text

class StringText(private val content: String) : Text() {
    override fun toString(): String {
        return content
    }
}