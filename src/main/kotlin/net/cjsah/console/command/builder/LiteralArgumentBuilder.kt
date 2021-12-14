package net.cjsah.console.command.builder

import net.cjsah.console.command.tree.LiteralCommandNode

class LiteralArgumentBuilder private constructor(private val literal: String) : ArgumentBuilder<LiteralArgumentBuilder>() {

    companion object{
        @JvmStatic
        fun literal(literal: String) = LiteralArgumentBuilder(literal)
    }

    override fun getThis() = this

    override fun build() = LiteralCommandNode(getHelp(), literal, getCommand(), getRequirement()).apply {
        getArguments().forEach { this.addChild(it) }
    }

}