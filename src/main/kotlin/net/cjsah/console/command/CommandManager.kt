package net.cjsah.console.command

import net.cjsah.console.Console.logger
import net.cjsah.console.command.argument.Argument
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.builder.RequiredArgumentBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.exceptions.CommandException
import java.util.function.Consumer

object CommandManager {
    @JvmStatic
    private val DISPATCHER = Dispatcher()

    @JvmStatic
    fun literal(literal: String): LiteralArgumentBuilder {
        return LiteralArgumentBuilder.literal(literal)
    }

    @JvmStatic
    fun <T> argument(literal: String, type: Argument<T>): RequiredArgumentBuilder<T> {
        return RequiredArgumentBuilder.argument(literal, type)
    }

    @JvmStatic
    fun execute(command: String, source: CommandSource<*>): Int {
        try {
            return DISPATCHER.execute(command, source)
        } catch (e: CommandException) {
            e.message?.let { logger.error(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    @JvmStatic
    fun register(command: Consumer<Dispatcher>) {
        command.accept(DISPATCHER)
    }

}