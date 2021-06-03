package net.cjsah.bot.console.command.builder

import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.tree.ArgumentCommandNode
import net.cjsah.bot.console.command.tree.CommandNode

class RequiredArgumentBuilder<T>(val name: String, val type: ArgumentType<T>) : ArgumentBuilder<RequiredArgumentBuilder<T>>() {

    companion object {
        fun <T> argument(name: String, type: ArgumentType<T>) = RequiredArgumentBuilder(name, type)
    }

    override fun getThis() = this

    override fun build(): CommandNode {
        val result: ArgumentCommandNode<T> = ArgumentCommandNode(name, type, getCommand(), getRequirement())

        for (argument in getArguments()) {
            result.addChild(argument)
        }

        return result
    }
}