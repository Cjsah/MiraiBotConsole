package net.cjsah.console.command;

import com.google.common.collect.Lists;
import net.cjsah.console.command.builder.LiteralArgumentBuilder;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.command.context.CommandContextBuilder;
import net.cjsah.console.command.source.CommandSource;
import net.cjsah.console.command.tree.CommandNode;
import net.cjsah.console.command.tree.RootCommandNode;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private static final char ARGUMENT_SEPARATOR = ' ';

    private static final RootCommandNode ROOTS = new RootCommandNode();

    public void register(LiteralArgumentBuilder command) {
        ROOTS.addChild(command.build());
    }

    protected int execute(String input, CommandSource<?> source) throws CommandException {
        StringReader reader = new StringReader(input);
        CommandContextBuilder builder = new CommandContextBuilder(this, source, ROOTS, reader.getCursor());
        ParseResults parse = parseNodes(ROOTS, reader, builder);
        if (reader.canRead()) {
            if (parse.getExceptions().size() == 1) throw parse.getExceptions().values().iterator().next();
            else if (parse.getContext().getRange().isEmpty()) throw BuiltExceptions.dispatcherUnknownCommand().createWithContext(reader);
            else throw BuiltExceptions.dispatcherUnknownArgument().createWithContext(reader);
        }
        int result = 0;
        boolean foundCommand = false;
        CommandContext original = parse.getContext().build();
        List<CommandContext> contexts = Lists.newArrayList(original);
        while (contexts != null) {
            for (CommandContext context : contexts) {
                if (context.getCommand() != null) {
                    foundCommand = true;
                    try {
                        context.getCommand().run(context);
                        result += 1;
                    } catch (CommandException e) {
                        e.printStackTrace();
                    }
                }
            }
            contexts = null;
        }
        if (!foundCommand) {
            throw BuiltExceptions.dispatcherUnknownCommand().createWithContext(reader);
        }
        return result;
    }

    private ParseResults parseNodes(CommandNode node, StringReader originalReader, CommandContextBuilder builder) {
        Map<CommandNode, CommandException> exceptions = new LinkedHashMap<>();
        CommandSource<?> source = builder.getSource();
        List<ParseResults> potentials = new ArrayList<>();
        int cursor = originalReader.getCursor();
        for (CommandNode child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            CommandContextBuilder context = builder.copy();
            StringReader reader = originalReader.copy();
            try {
                try {
                    child.parse(reader, context);
                }catch (RuntimeException e) {
                    throw BuiltExceptions.dispatcherParseException().createWithContext(reader, e.getLocalizedMessage());
                }
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR) throw BuiltExceptions.dispatcherExpectedArgumentSeparator().createWithContext(reader);
            } catch (CommandException e) {
                exceptions.put(child, e);
                reader.setCursor(cursor);
                continue;
            }
            context.withCommand(child.getCommand());
            if (reader.canRead(2)) {
                reader.skip();
                ParseResults parse = parseNodes(child, reader, context);
                potentials.add(parse);
            } else {
                potentials.add(new ParseResults(context, reader, Collections.emptyMap()));
            }
        }
        if (!potentials.isEmpty()) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }
        return new ParseResults(builder, originalReader, exceptions);
    }

    public Map<String, String> getHelp(CommandSource<?> source) {
        final Map<String, String> result = new LinkedHashMap<>();
        ROOTS.getChildren().forEach((child) -> this.getHelp(result, "/", child, source, true));
        return result;
    }

    private void getHelp(final Map<String, String> result, String mem, CommandNode node, CommandSource<?> source, boolean first) {
        if (!node.canUse(source)) return;
        mem += String.format("%s%s", first ? "" : " ", node.getUsageText());
        if (node.getCommand() != null) {
            result.put(mem, node.getHelp());
        }
        String finalMem = mem;
        node.getChildren().forEach((child) -> this.getHelp(result, finalMem, child, source, false));
    }
}
