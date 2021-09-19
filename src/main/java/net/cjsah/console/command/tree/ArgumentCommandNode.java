package net.cjsah.console.command.tree;

import net.cjsah.console.command.argument.Argument;
import net.cjsah.console.command.builder.ArgumentBuilder;
import net.cjsah.console.command.Command;
import net.cjsah.console.command.builder.RequiredArgumentBuilder;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.command.context.ParsedNodeResult;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.exceptions.CommandException;

import java.util.function.Predicate;

public class ArgumentCommandNode<T> extends CommandNode{
    private final String name;
    private final Argument<T> type;

    public ArgumentCommandNode(String help, final String name, final Argument<T> type, Command command, Predicate<CommandSource<?>> requirement) {
        super(help, command, requirement);
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsageText() {
        return "<" + this.name + ">";
    }

    @Override
    protected boolean isValidInput(String input) {
        try {
            StringReader reader = new StringReader(input);
            this.type.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        } catch (CommandException ignored) {
            return false;
        }
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder builder) throws CommandException {
        int start = reader.getCursor();
        T result = this.type.parse(reader);
        ParsedNodeResult<T> parsed = new ParsedNodeResult<>(start, reader.getCursor(), result);
        builder.withArgument(this.name, parsed).withRange(parsed.getRange());
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        RequiredArgumentBuilder<T> builder = RequiredArgumentBuilder.argument(this.name, this.type);
        builder.requires(getRequirement());
        if (getCommand() != null) {
            builder.executes(getHelp(), getCommand());
        }
        return builder;
    }

    @Override
    protected String getSortedKey() {
        return this.name;
    }
}
