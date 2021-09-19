package net.cjsah.console.command.builder;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.command.tree.CommandNode;
import net.cjsah.console.command.tree.RootCommandNode;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<T extends ArgumentBuilder<T>> {
    private final RootCommandNode arguments = new RootCommandNode();
    private Predicate<CommandSource<?>> requirement = (source) -> true;
    private Command command = null;
    private String help = "";

    public T then(ArgumentBuilder<?> argument) {
        this.arguments.addChild(argument.build());
        return getThis();
    }

    public T executes(String help, Command command) {
        this.command = command;
        this.help = help;
        return getThis();
    }

    public T requires(Predicate<CommandSource<?>> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    protected abstract T getThis();

    public abstract CommandNode build();

    public Collection<CommandNode> getArguments() {
        return arguments.getChildren();
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    public Command getCommand() {
        return this.command;
    }

    public String getHelp() {
        return this.help;
    }
}
