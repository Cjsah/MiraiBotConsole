package net.cjsah.console.command

import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.exceptions.CommandException

class ParseResults(
    val context: ContextBuilder,
    val reader: StringReader,
    val exceptions: Map<CommandNode, CommandException>
)