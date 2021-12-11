@file:Suppress("UNCHECKED_CAST")

package net.cjsah.console.command.context

import net.cjsah.console.command.Command
import net.cjsah.console.command.source.CommandSource

class CommandContext(
    private val arguments: Map<String, ParsedNodeResult<*>>,
    private val source: CommandSource<*>,
    private val command: Command?
) {
    fun <V> getArgument(name: String, clazz: Class<V>): V {
        val argument: ParsedNodeResult<*> = arguments[name] ?: throw IllegalArgumentException("此命令中不存在参数 '$name'")
        val result = argument.result
        return if (ClassConversion[clazz].isAssignableFrom(result.javaClass)) {
            result as V
        } else {
            throw IllegalArgumentException("参数 '" + name + "' 被定义为 " + result.javaClass.simpleName + ", 而不是 " + clazz)
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