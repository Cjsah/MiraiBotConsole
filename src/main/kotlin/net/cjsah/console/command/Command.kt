package net.cjsah.console.command

import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.CommandException

fun interface Command {
    companion object {
        const val SUCCESSFUL = 1
    }

    @Throws(CommandException::class)
    suspend fun run(context: CommandContext): Int
}