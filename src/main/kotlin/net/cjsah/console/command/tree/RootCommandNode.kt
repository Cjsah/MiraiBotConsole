package net.cjsah.console.command.tree

import net.cjsah.console.Console
import net.cjsah.console.command.StringReader
import net.cjsah.console.command.builder.ArgumentBuilder
import net.cjsah.console.command.context.ContextBuilder
import net.cjsah.console.exceptions.CommandException
import net.cjsah.console.exceptions.Para0CommandException
import net.cjsah.console.plugin.Plugin

class RootCommandNode : CommandNode("", null, {true})  {
    override fun getName() = ""

    override fun getUsageText() = ""

    override fun isValidInput(input: String) = false

    fun addChild(plugin: Plugin, node: CommandNode) {
        node.setPlugin(plugin)
        super.addChild(node)
    }

    @Throws(CommandException::class)
    override fun parse(reader: StringReader, builder: ContextBuilder) {
    }

    override fun createBuilder(): ArgumentBuilder<*> {
        throw IllegalStateException("无法将Root转换为builder")
    }

    override fun getSortedKey() = ""
}