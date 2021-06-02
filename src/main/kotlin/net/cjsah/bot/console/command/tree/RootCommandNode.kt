package net.cjsah.bot.console.command.tree

import net.cjsah.bot.console.command.StringReader
import net.cjsah.bot.console.command.builder.ArgumentBuilder
import net.cjsah.bot.console.command.context.CommandContextBuilder

class RootCommandNode<S> : CommandNode<S>(null, { true }) {

    override fun getName() = ""

    override fun isValidInput(input: String) = false

    override fun parse(reader: StringReader, contextBuilder: CommandContextBuilder<S>) { }

    override fun createBuilder(): ArgumentBuilder<S, *> {
        throw IllegalStateException("Cannot convert root into a builder")
    }

    override fun getSortedKey() = ""

}