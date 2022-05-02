package net.cjsah.console.command

import net.cjsah.console.Logger
import net.cjsah.console.command.argument.Argument
import net.cjsah.console.command.builder.LiteralArgumentBuilder
import net.cjsah.console.command.builder.RequiredArgumentBuilder
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.plugin.Plugin
import java.util.function.Consumer

object CommandManager {
    @JvmStatic
    private val DISPATCHER = Dispatcher()

    @JvmStatic
    fun literal(literal: String): LiteralArgumentBuilder =
        LiteralArgumentBuilder.literal(literal)


    @JvmStatic
    fun <T> argument(literal: String, type: Argument<T>): RequiredArgumentBuilder<T> =
        RequiredArgumentBuilder.argument(literal, type)


    @JvmStatic
    suspend fun execute(command: String, source: CommandSource<*>): Int {
        try {
            return DISPATCHER.execute(command, source)
        } catch (e: Exception) {
            e.message?.let { Logger.error(it) }
        }
        return 0
    }

    @JvmStatic
    fun register(command: Consumer<Dispatcher>) = command.accept(DISPATCHER)

    @JvmStatic
    internal fun deregister(plugin: Plugin) = DISPATCHER.deregister(plugin)

}