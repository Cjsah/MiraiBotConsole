package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.exceptions.CommandException
import net.cjsah.bot.console.command.tree.CommandNode

class ParseResults(
    private val context: CommandContextBuilder,
    private val reader: StringReaderProvider,
    private val exceptions: Map<CommandNode, CommandException>
) {
    constructor(context: CommandContextBuilder) : this(context, StringReader(""), emptyMap<CommandNode, CommandException>())

    fun getContext() = context

    fun getReader() = reader

    fun getExceptions() = exceptions

}