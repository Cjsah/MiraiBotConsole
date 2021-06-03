package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.builder.LiteralArgumentBuilder
import net.cjsah.bot.console.command.builder.RequiredArgumentBuilder

object CommandManager {

    val dispatcher = Dispatcher()

    fun literal(literal: String) = LiteralArgumentBuilder.literal(literal)

    fun <T> argument(name: String, type: ArgumentType<T>) = RequiredArgumentBuilder.argument(name, type)

    fun execute(command: String, source: CommandSource) {
        try {
            this.dispatcher.execute(command, source)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}