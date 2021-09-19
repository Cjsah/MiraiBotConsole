package net.cjsah.console.command.tree;

import net.cjsah.console.command.builder.ArgumentBuilder;
import net.cjsah.console.command.Command;
import net.cjsah.console.command.builder.LiteralArgumentBuilder;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.command.context.IntRange;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

import java.util.function.Predicate;

public class LiteralCommandNode extends CommandNode {
    private final String literal;

    public LiteralCommandNode(final String help, final String literal, Command command, Predicate<CommandSource<?>> requirement) {
        super(help, command, requirement);
        this.literal = literal;
    }

    @Override
    public String getName() {
        return this.literal;
    }

    @Override
    public String getUsageText() {
        return this.literal;
    }

    @Override
    protected boolean isValidInput(String input) {
        return parse(new StringReader(input)) > -1;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder builder) throws CommandException {
        int start = reader.getCursor();
        int end = parse(reader);
        if (end > -1) {
            builder.withRange(new IntRange(start, end));
            return;
        }
        throw BuiltExceptions.literalIncorrect().createWithContext(reader, this.literal);
    }

    @Override
    public ArgumentBuilder<?> createBuilder() {
        LiteralArgumentBuilder builder = LiteralArgumentBuilder.literal(this.literal);
        builder.requires(getRequirement());
        if (getCommand() != null) builder.executes(getHelp(), getCommand());
        return builder;
    }

    @Override
    protected String getSortedKey() {
        return this.literal;
    }

    private int parse(StringReader reader) {
        if (reader.canRead(this.literal.length())) {
            int start = reader.getCursor();
            int end = start +this.literal.length();
            if (reader.getString().substring(start, end).equals(this.literal)) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                }else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }
}
