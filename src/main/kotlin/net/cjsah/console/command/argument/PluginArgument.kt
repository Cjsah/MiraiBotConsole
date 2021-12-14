@file:Suppress("unused")

package net.cjsah.console.command.argument

import net.cjsah.console.command.StringReader
import net.cjsah.console.command.context.CommandContext
import net.cjsah.console.exceptions.BuiltExceptions
import net.cjsah.console.plugin.Plugin
import net.cjsah.console.plugin.PluginLoader

class PluginArgument private constructor() : Argument<Plugin> {

    companion object {
        @JvmStatic
        fun plugin() = PluginArgument()

        @JvmStatic
        fun getPlugin(context: CommandContext, name: String) = context.getArgument(name, Plugin::class.java)
    }

    override fun parse(reader: StringReader): Plugin {
        val start = reader.getCursor()
        val id = reader.readUnquotedString()
        val plugin = PluginLoader.getPlugin(id)
        if (plugin == null) {
            reader.setCursor(start)
            throw BuiltExceptions.pluginNotFound().createWithContext(reader, id)
        }
        return plugin
    }
}