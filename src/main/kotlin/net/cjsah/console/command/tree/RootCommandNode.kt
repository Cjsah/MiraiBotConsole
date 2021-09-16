package net.cjsah.console.command.tree

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.context.CommandContextBuilder

class RootCommandNode : CommandNode(null, { true }) {

    override fun getName() = ""
    override fun getUsageText() = ""

    override fun isValidInput(input: String) = false

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder) { }

    override fun createBuilder(): ArgumentBuilder<*> {
        throw IllegalStateException("Cannot convert root into a builder")
    }

    override fun getSortedKey() = ""

}