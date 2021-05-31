package net.cjsah.bot.console

import com.google.common.collect.Lists
import net.cjsah.bot.console.command.ArgumentBuilder
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.SourceType
import net.cjsah.bot.console.command.argument.RequiredArgumentType
import net.mamoe.mirai.contact.User

object Commands {

    private val CONSOLE_COMMANDS = HashMap<Plugin, List<ArgumentBuilder>>()
    private val USER_COMMANDS = HashMap<Plugin, List<ArgumentBuilder>>()

    internal fun runCommand(command: String?, type: SourceType, user: User?) {
        try {
            val notes = command!!.split(" ")
            val lists = (if (type == SourceType.CONSOLE) this.CONSOLE_COMMANDS else this.USER_COMMANDS).values
            val source = CommandSource(type, user)
            var argument: ArgumentBuilder? = getArgumentBuilder(lists, (notes as ArrayList).removeFirst())
            while (notes.isNotEmpty()) {
                val target = argument!!.target
                val note = notes.removeFirst()
                if (target is RequiredArgumentType<*>) {
                    source.commands[target.name] = note
                }
                argument = argument.getNext(note)
            }
            argument!!.runCommand(source)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getArgumentBuilder(lists: MutableCollection<List<ArgumentBuilder>>, name: String): ArgumentBuilder? {
        for (list in lists) {
            for (argumentBuilder in list) {
                if (argumentBuilder.target.isThisCommand(name)) {
                    return argumentBuilder
                }
            }
        }
        return null
    }

    internal fun register(type: SourceType, plugin: Plugin, command: ArgumentBuilder) {
        val map = if (type == SourceType.CONSOLE) this.CONSOLE_COMMANDS else this.USER_COMMANDS
        (map.getOrDefault(plugin, Lists.newArrayList()) as ArrayList).add(command)
        if (!map.contains(plugin)) (map[plugin] as ArrayList).add(command)
        else {
            val list = Lists.newArrayList<ArgumentBuilder>()
            list.add(command)
            map[plugin] = list
        }
    }
}