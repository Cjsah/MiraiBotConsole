package net.cjsah.console.command.builder

import net.cjsah.console.command.tree.CommandNode
import net.cjsah.console.command.tree.LiteralCommandNode

class LiteralArgumentBuilder internal constructor(private val literal: String) : ArgumentBuilder<LiteralArgumentBuilder>() {

    companion object {
        fun literal(literal: String) = LiteralArgumentBuilder(literal)
    }

    override fun getThis() = this

    override fun build(): CommandNode {
        val result = LiteralCommandNode(literal, getCommand(), getRequirement())

        for (argument in getArguments()) {
            result.addChild(argument)
        }

        return result
    }
}