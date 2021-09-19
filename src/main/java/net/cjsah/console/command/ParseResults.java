package net.cjsah.console.command;

import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.command.tree.CommandNode;
import net.cjsah.console.exceptions.CommandException;

import java.util.Map;

public class ParseResults {
    private final CommandContextBuilder context;
    private final StringReader reader;
    private final Map<CommandNode, CommandException> exceptions;

    public ParseResults(final CommandContextBuilder context, final StringReader reader, final Map<CommandNode, CommandException> exceptions) {
        this.context = context;
        this.reader = reader;
        this.exceptions = exceptions;
    }

    public CommandContextBuilder getContext() {
        return this.context;
    }

    public StringReader getReader() {
        return this.reader;
    }

    public Map<CommandNode, CommandException> getExceptions() {
        return this.exceptions;
    }
}
