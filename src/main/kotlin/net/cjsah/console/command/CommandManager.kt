package net.cjsah.console.command

import net.cjsah.console.Console
import net.cjsah.console.command.arguments.base.Argument
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.builder.RequiredArgumentBuilder
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.command.source.CommandSource

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