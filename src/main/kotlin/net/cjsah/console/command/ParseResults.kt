package net.cjsah.console.command

import net.cjsah.console.command.context.CommandContextBuilder
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.command.tree.CommandNode

class ParseResults(
    val context: CommandContextBuilder,
    val reader: StringReaderProvider,
    val exceptions: Map<CommandNode, CommandException>
)