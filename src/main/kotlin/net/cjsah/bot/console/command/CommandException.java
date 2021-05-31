package net.cjsah.bot.console.command;

public class CommandException {

    public static final CException UNKNOWN_COMMAND = new UnknownCommandException();
    public static final CException PARAMETER_ERROR = new ParameterErrorException();

    static class CException extends Exception {
        protected CException(String e) {
            super(e);
        }
    }

    static class UnknownCommandException extends CException {
        private UnknownCommandException() {
            super("Unknown Command!");
        }
    }

    static class ParameterErrorException extends CException {
        private ParameterErrorException() {
            super("Parameter Error!");
        }
    }

}
