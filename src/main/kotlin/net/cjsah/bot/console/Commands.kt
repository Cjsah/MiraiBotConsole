package net.cjsah.bot.console

import com.google.common.collect.Lists
import net.cjsah.bot.console.command.CommandSource
import net.cjsah.bot.console.command.SourceType
import net.cjsah.bot.console.command.argument.RequiredArgumentType
import net.mamoe.mirai.contact.User
import java.util.stream.Collectors

object Commands {

    private val CONSOLE_COMMANDS = HashMap<Plugin<*>, List<ArgumentBuilder>>()
    private val USER_COMMANDS = HashMap<Plugin<*>, List<ArgumentBuilder>>()

    internal fun runCommand(command: String?, type: SourceType, user: User?) {
        try {
            val notes = command!!.split(" ").stream().collect(Collectors.toList())
            val lists = (if (type == SourceType.CONSOLE) this.CONSOLE_COMMANDS else this.USER_COMMANDS).values
            val source = CommandSource(type, user)
            var argument: ArgumentBuilder? = getArgumentBuilder(lists, notes.removeFirst(), source)
            notes.forEach {
                val target = argument!!.target
                if (target is RequiredArgumentType<*>) {
                    source.commands[target.name] = it
                }
                argument = argument!!.getNext(it, source)
            }
//            argument?.runCommand(source) ?: throw CommandException.UNKNOWN_COMMAND
//        }catch (e: CommandException.UnknownCommandException) {
//            e.message?.let { Console.logger.log(it) }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getArgumentBuilder(lists: MutableCollection<List<ArgumentBuilder>>, name: String, source: CommandSource): ArgumentBuilder? {
        for (list in lists) {
            for (argumentBuilder in list) {
                if (argumentBuilder.target.isThisCommand(name) && argumentBuilder.isRequirement(source)) {
                    return argumentBuilder
                }
            }
        }
        return null
    }

    internal fun <T> register(type: SourceType, plugin: Plugin<T>, command: ArgumentBuilder) {
        val map = if (type == SourceType.CONSOLE) this.CONSOLE_COMMANDS else this.USER_COMMANDS
        (map.getOrDefault(plugin, Lists.newArrayList()) as ArrayList).add(command)
        if (map.contains(plugin)) (map[plugin] as ArrayList).add(command)
        else {
            val list = Lists.newArrayList<ArgumentBuilder>()
            list.add(command)
            map[plugin] = list
        }
    }
}