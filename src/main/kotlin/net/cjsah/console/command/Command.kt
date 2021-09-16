package net.cjsah.console.command

import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.CommandException

fun interface Command {
    @Throws(CommandException::class)
    fun run(context: CommandContext)
}
