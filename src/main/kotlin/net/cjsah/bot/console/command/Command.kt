package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException

fun interface Command {
    @Throws(CommandException::class)
    fun run(context: CommandContext)
}
