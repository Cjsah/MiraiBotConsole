package net.cjsah.console.command.builder;

import net.cjsah.console.command.tree.CommandNode;
import net.cjsah.console.command.tree.LiteralCommandNode;

public class LiteralArgumentBuilder extends ArgumentBuilder<LiteralArgumentBuilder>{
    private final String literal;

    private LiteralArgumentBuilder(final String literal) {
        this.literal = literal;
    }

    public static LiteralArgumentBuilder literal(String literal) {
        return new LiteralArgumentBuilder(literal);
    }

    @Override
    protected LiteralArgumentBuilder getThis() {
        return this;
    }

    @Override
    public CommandNode build() {
        CommandNode result = new LiteralCommandNode(this.getHelp(), this.literal, getCommand(), getRequirement());
        getArguments().forEach(result::addChild);
        return result;
    }

}
