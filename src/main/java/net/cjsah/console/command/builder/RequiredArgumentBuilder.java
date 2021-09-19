package net.cjsah.console.command.builder;

import net.cjsah.console.command.argument.Argument;
import net.cjsah.console.command.tree.ArgumentCommandNode;
import net.cjsah.console.command.tree.CommandNode;

public class RequiredArgumentBuilder<T> extends ArgumentBuilder<RequiredArgumentBuilder<T>>{
    private final String name;
    private final Argument<T> type;

    private RequiredArgumentBuilder(final String name, final Argument<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <S> RequiredArgumentBuilder<S> argument(String name, Argument<S> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    @Override
    protected RequiredArgumentBuilder<T> getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        CommandNode result = new ArgumentCommandNode<>(this.getHelp(), this.name, this.type, getCommand(), getRequirement());
        getArguments().forEach(result::addChild);
        return result;
    }
}
