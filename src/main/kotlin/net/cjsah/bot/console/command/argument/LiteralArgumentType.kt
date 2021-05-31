package net.cjsah.bot.console.command.argument

class LiteralArgumentType(private val command: String): ArgumentType {
    override fun isThisCommand(content: String): Boolean {
        return content == command
    }
}