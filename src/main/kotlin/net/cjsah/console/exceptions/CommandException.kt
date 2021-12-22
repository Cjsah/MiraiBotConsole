@file:Suppress("unused")

package net.cjsah.console.exceptions

class CommandException(
    val type: CommandExceptionType,
    message: String,
    val input: String?,
    val cursor: Int
) : Exception(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES) {

    companion object {
        @JvmField
        var ENABLE_COMMAND_STACK_TRACES = true
    }

    constructor(type: CommandExceptionType, message: String) : this(type, message, null, -1)

}