package net.cjsah.console.command.tree;

import net.cjsah.console.command.builder.ArgumentBuilder;
import net.cjsah.console.command.Command;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class CommandNode implements Comparable<CommandNode> {
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode<?>> arguments = new LinkedHashMap<>();
    private final Predicate<CommandSource<?>> requirement;
    private Command command;
    private String help;

    public CommandNode(String help, Command command, final Predicate<CommandSource<?>> requirement) {
        this.help = help;
        this.command = command;
        this.requirement = requirement;
    }

    public abstract String getName();

    public abstract String getUsageText();

    protected abstract boolean isValidInput(String input);

    public abstract void parse(StringReader reader, CommandContextBuilder builder) throws CommandException;

    public abstract ArgumentBuilder<?> createBuilder();

    protected abstract String getSortedKey();

    public Collection<CommandNode> getChildren() {
        return this.children.values();
    }

    public Command getCommand() {
        return this.command;
    }

    public String getHelp() {
        return this.help;
    }

    public Predicate<CommandSource<?>> getRequirement() {
        return this.requirement;
    }

    public boolean canUse(CommandSource<?> source) {
        return this.requirement.test(source);
    }

    public void addChild(CommandNode node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("无法将 'RootCommandNode' 作为一个子节点添加到其他 'CommandNode'");
        }
        CommandNode child = this.children.get(node.getName());
        if (child != null) {
            if (node.command != null) {
                child.command = node.command;
            }
            if (node.help != null) {
                child.help = node.help;
            }
            for (CommandNode nodeChild : node.getChildren()) {
                child.addChild(nodeChild);
            }
        } else {
            this.children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                this.literals.put(node.getName(), (LiteralCommandNode) node);
            } else if (node instanceof ArgumentCommandNode) {
                this.arguments.put(node.getName(), (ArgumentCommandNode<?>) node);
            }
        }
    }

    public Collection<? extends CommandNode> getRelevantNodes(StringReader input) {
        if (!this.literals.isEmpty()) {
            int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            LiteralCommandNode literal = this.literals.get(text);
            if (literal != null) return Collections.singletonList(literal);
            else return this.arguments.values();
        }
        return this.arguments.values();
    }

    @Override
    public int hashCode() {
        return 31 * this.children.hashCode() + (this.command != null ? command.hashCode() : 0);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CommandNode)) return false;
        if (this.children != ((CommandNode) other).children) return false;
        return !(this.command != null ? this.command != ((CommandNode) other).command : ((CommandNode) other).command != null);
    }

    @Override
    public int compareTo(@NotNull CommandNode node) {
        if ((this instanceof LiteralCommandNode) == (node instanceof LiteralCommandNode)) {
            return getSortedKey().compareTo(node.getSortedKey());
        }
        return node instanceof LiteralCommandNode ? 1 : -1;
    }
}
