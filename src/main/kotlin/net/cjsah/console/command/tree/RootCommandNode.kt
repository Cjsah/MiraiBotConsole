package net.cjsah.console.command.tree

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.exceptions.CommandException

class RootCommandNode : CommandNode("", null, {true})  {
    override fun getName() = ""

    override fun getUsageText() = ""

    override fun isValidInput(input: String) = false

    @Throws(CommandException::class)
    override fun parse(reader: StringReader, builder: ContextBuilder) {
    }

    override fun createBuilder(): ArgumentBuilder<*> {
        throw IllegalStateException("无法将Root转换为builder")
    }

    override fun getSortedKey() = ""
}