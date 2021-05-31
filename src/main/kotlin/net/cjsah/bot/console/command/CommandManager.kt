package net.cjsah.bot.console.command

import net.cjsah.bot.console.command.argument.RequiredArgumentType
import net.cjsah.bot.console.command.argument.LiteralArgumentType

class CommandManager {

    fun literal(literal: String): ArgumentBuilder = ArgumentBuilder(LiteralArgumentType(literal))

    fun <T : RequiredArgumentType<T>> argument(name: String, type: RequiredArgumentType<T>) = ArgumentBuilder(type.withName(name))

}