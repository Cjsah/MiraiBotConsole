package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.context.CommandContext
import net.cjsah.bot.console.command.exceptions.CommandException

fun interface Command<S> {
    @Throws(CommandException::class)
    fun run(context: CommandContext<S>?): Int

    companion object {
        const val SINGLE_SUCCESS = 1
    }
}
