@file:Suppress("UNCHECKED_CAST")

package net.cjsah.console.command.context

import net.cjsah.console.command.Command
import net.cjsah.console.command.source.CommandSource
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.text.TranslateText

class CommandContext(
    private val arguments: Map<String, ParsedNode<*>>,
    private val source: CommandSource<*>,
    private val command: Command?
) {
    fun <V> getArgument(name: String, clazz: Class<V>): V {
        val argument: ParsedNode<*> = arguments[name] ?:
        throw ConsoleException.create(
            TranslateText("command.noargs", name),
            IllegalArgumentException::class.java
        )
        val result = argument.getResult()!!
        return if (ClassConversion[clazz].isAssignableFrom(result.javaClass)) {
            result as V
        } else {
            throw ConsoleException.create(
                TranslateText("command.args.invalid", name, result.javaClass.simpleName, clazz),
                IllegalArgumentException::class.java
            )
        }
    }

    fun getSource(): CommandSource<*> {
        return source
    }

    fun getCommand(): Command? {
        return command
    }

    internal object ClassConversion {
        private val PRIMITIVE_TO_WRAPPER: MutableMap<Class<*>?, Class<*>> = HashMap()
        operator fun get(clazz: Class<*>): Class<*> {
            return PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz)
        }

        init {
            PRIMITIVE_TO_WRAPPER[Boolean::class.javaPrimitiveType] = Boolean::class.java
            PRIMITIVE_TO_WRAPPER[Byte::class.javaPrimitiveType] = Byte::class.java
            PRIMITIVE_TO_WRAPPER[Short::class.javaPrimitiveType] = Short::class.java
            PRIMITIVE_TO_WRAPPER[Char::class.javaPrimitiveType] = Char::class.java
            PRIMITIVE_TO_WRAPPER[Int::class.javaPrimitiveType] = Int::class.java
            PRIMITIVE_TO_WRAPPER[Long::class.javaPrimitiveType] = Long::class.java
            PRIMITIVE_TO_WRAPPER[Float::class.javaPrimitiveType] = Float::class.java
            PRIMITIVE_TO_WRAPPER[Double::class.javaPrimitiveType] = Double::class.java
        }
    }

}