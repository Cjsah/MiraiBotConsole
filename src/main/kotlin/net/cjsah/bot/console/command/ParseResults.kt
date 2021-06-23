package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.context.CommandContextBuilder
import net.cjsah.bot.console.command.exceptions.CommandException
import net.cjsah.bot.console.command.tree.CommandNode

class ParseResults(
    val context: CommandContextBuilder,
    val reader: StringReaderProvider,
    val exceptions: Map<CommandNode, CommandException>
)