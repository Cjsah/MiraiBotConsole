package net.cjsah.console.command.tree;

import net.cjsah.console.command.builder.ArgumentBuilder;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.exceptions.CommandException;

public class RootCommandNode extends CommandNode{
    public RootCommandNode() {
        super(null, null, (source) -> true);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getUsageText() {
        return "";
    }

    @Override
    protected boolean isValidInput(String input) {
        return false;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder builder) throws CommandException {

    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        throw new IllegalStateException("Cannot convert root into a builder");
    }

    @Override
    protected String getSortedKey() {
        return "";
    }
}
