package net.cjsah.bot.console.command

import net.cjsah.bot.console.Console
import net.cjsah.bot.console.command.arguments.base.Argument
import net.cjsah.bot.console.command.builder.LiteralArgumentBuilder
import net.cjsah.bot.console.command.builder.RequiredArgumentBuilder
import net.cjsah.bot.console.exceptions.CommandException
import net.cjsah.bot.console.command.source.CommandSource

object CommandManager {

    internal val dispatcher = Dispatcher()

    fun literal(literal: String) = LiteralArgumentBuilder.literal(literal)

    fun <T> argument(name: String, type: Argument<T>) = RequiredArgumentBuilder.argument(name, type)

    internal fun execute(command: String, source: CommandSource<*>) {
        try {
            this.dispatcher.execute(command, source)
        } catch (e: CommandException) {
            e.message?.let { Console.logger.error(e.message) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}