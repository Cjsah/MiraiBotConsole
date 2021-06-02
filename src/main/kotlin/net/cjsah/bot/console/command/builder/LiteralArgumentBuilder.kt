package net.cjsah.bot.console.command.builder

import net.cjsah.bot.console.command.tree.CommandNode
import net.cjsah.bot.console.command.tree.LiteralCommandNode

class LiteralArgumentBuilder<S> internal constructor(val literal: String) : ArgumentBuilder<S, LiteralArgumentBuilder<S>>() {

    companion object {
        fun <S> literal(literal: String) = LiteralArgumentBuilder<S>(literal)
    }

    override fun getThis() = this

    override fun build(): CommandNode<S> {
        val result: LiteralCommandNode<S> = LiteralCommandNode(literal, getCommand(), getRequirement())

        for (argument in getArguments()) {
            result.addChild(argument)
        }

        return result
    }
}