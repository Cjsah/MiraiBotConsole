package net.cjsah.bot.console.command.builder

import net.cjsah.bot.console.command.arguments.ArgumentType
import net.cjsah.bot.console.command.tree.ArgumentCommandNode
import net.cjsah.bot.console.command.tree.CommandNode

class RequiredArgumentBuilder<S, T>(val name: String, val type: ArgumentType<T>) : ArgumentBuilder<S, RequiredArgumentBuilder<S, T>>() {

    companion object {
        fun <S, T> argument(name: String, type: ArgumentType<T>) = RequiredArgumentBuilder<S, T>(name, type)
    }

    override fun getThis() = this

    override fun build(): CommandNode<S> {
        val result: ArgumentCommandNode<S, T> = ArgumentCommandNode(name, type, getCommand(), getRequirement())

        for (argument in getArguments()) {
            result.addChild(argument)
        }

        return result
    }
}