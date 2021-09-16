package net.cjsah.console.exceptions


class CommandException: Exception {
    companion object {
        val BUILT_EXCEPTIONS = BuiltExceptions()

        var ENABLE_COMMAND_STACK_TRACES = true
    }

    private val type: CommandExceptionType
    private val input: String?
    private val cursor: Int

    constructor(type: CommandExceptionType, message: String) : super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES) {
        this.type = type
        this.input = null
        this.cursor = -1
    }

    constructor(type: CommandExceptionType, message: String, input: String, cursor: Int) : super(message, null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES) {
        this.type = type
        this.input = input
        this.cursor = cursor
    }

    fun getCursor() = cursor
}