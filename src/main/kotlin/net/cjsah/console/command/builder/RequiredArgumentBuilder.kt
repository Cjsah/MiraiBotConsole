package net.cjsah.console.command.builder

import net.cjsah.console.command.argument.Argument
import net.cjsah.console.command.tree.ArgumentCommandNode

class RequiredArgumentBuilder<T> private constructor(
    private val name: String,
    private val type: Argument<T>
): ArgumentBuilder<RequiredArgumentBuilder<T>>() {

    companion object {
        @JvmStatic
        fun <S> argument(name: String, type: Argument<S>) = RequiredArgumentBuilder(name, type)
    }


    override fun getThis() = this

    override fun build() = ArgumentCommandNode(getHelp(), name, type, getCommand(), getRequirement()).apply {
        getArguments().forEach { this.addChild(it) }
    }

}