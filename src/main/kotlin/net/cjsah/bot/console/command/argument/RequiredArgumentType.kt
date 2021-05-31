package net.cjsah.bot.console.command.argument

abstract class RequiredArgumentType<T : RequiredArgumentType<T>> private constructor(val name: String): ArgumentType {
    abstract fun withName(name: String): T
}
