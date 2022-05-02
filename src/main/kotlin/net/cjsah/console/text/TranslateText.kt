package net.cjsah.console.text

import net.cjsah.console.Language

class TranslateText(private val translate: String, vararg args: Any) : Text() {
    private val args: Array<Any> = arrayOf(args)
    override fun toString() = Language.translate(translate).format(args)
}