package net.cjsah.console.command.builder

import net.cjsah.console.command.arguments.base.Argument
import net.cjsah.console.command.tree.ArgumentCommandNode
import net.cjsah.console.command.tree.CommandNode

class RequiredArgumentBuilder<T>(val name: String, private val type: Argument<T>) : ArgumentBuilder<RequiredArgumentBuilder<T>>() {

    companion object {
        fun <T> argument(name: String, type: Argument<T>) = RequiredArgumentBuilder(name, type)
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