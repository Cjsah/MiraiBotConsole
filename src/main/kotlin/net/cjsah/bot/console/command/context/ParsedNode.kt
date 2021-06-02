package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.tree.CommandNode

class ParsedNode<S>(val node: CommandNode<S>, range: IntRange)