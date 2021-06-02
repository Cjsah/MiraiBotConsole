package net.cjsah.bot.console.command

@Deprecated("")
object CommandException {

    val UNKNOWN_COMMAND: CException = UnknownCommandException()
    val PARAMETER_ERROR: CException = ParameterErrorException()

    open class CException protected constructor(e: String) : Exception(e)

    internal class UnknownCommandException : CException("Unknown Command!")

    internal class ParameterErrorException : CException("Parameter Error!")

}