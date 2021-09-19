package net.cjsah.console.command.context;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.Dispatcher;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.command.tree.CommandNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandContextBuilder {
    private final Map<String, ParsedNodeResult<?>> arguments;
    private final CommandSource<?> source;
    private final Dispatcher dispatcher;
    private final CommandNode rootNode;
    private Command command = null;
    private IntRange range;

    public CommandContextBuilder(final Dispatcher dispatcher, final CommandSource<?> source, final CommandNode rootNode, final int start) {
        this.dispatcher = dispatcher;
        this.source = source;
        this.rootNode = rootNode;
        this.arguments = new LinkedHashMap<>();
        this.range = new IntRange(start);
    }

    public CommandContextBuilder withArgument(String name, ParsedNodeResult<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public CommandContextBuilder withCommand(Command command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder withRange(IntRange range) {
        this.range = new IntRange(this.range, range);
        return this;
    }

    public CommandContextBuilder copy() {
        CommandContextBuilder builder = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.getStart());
        builder.command = this.command;
        builder.arguments.putAll(this.arguments);
        builder.range = this.range;
        return builder;
    }

    public CommandContext build() {
        return new CommandContext(this.arguments, this.source, this.command);
    }

    public CommandSource<?> getSource() {
        return this.source;
    }

    public IntRange getRange() {
        return this.range;
    }

}
