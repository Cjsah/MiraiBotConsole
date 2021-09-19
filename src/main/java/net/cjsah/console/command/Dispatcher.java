package net.cjsah.console.command;

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
        List<CommandContext> contexts = List.of(original);
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

//    fun getSmartUsage(source: CommandSource<*>): Map<CommandNode, String> {
//        val result: MutableMap<CommandNode, String> = LinkedHashMap<CommandNode, String>()
//        val optional = roots.getCommand() != null
//        for (child in roots.getChildren()) {
//            val usage: String? = getSmartUsage(child, source, optional, false)
//            if (usage != null) {
//                result[child] = usage
//            }
//        }
//        return result
//    }
//
//    private fun getSmartUsage(node: CommandNode, source: CommandSource<*>, optional: Boolean, deep: Boolean): String? {
//        if (!node.canUse(source)) {
//            return null
//        }
//        val self: String =
//        if (optional) USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE else node.getUsageText()
//        val childOptional = node.getCommand() != null
//        val open: String =
//        if (childOptional) USAGE_OPTIONAL_OPEN else USAGE_REQUIRED_OPEN
//        val close: String =
//        if (childOptional) USAGE_OPTIONAL_CLOSE else USAGE_REQUIRED_CLOSE
//        if (!deep) {
//            val children: Collection<CommandNode> = node.getChildren().stream().filter { c -> c.canUse(source) }.collect(Collectors.toList())
//            if (children.size == 1) {
//                val usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional)
//                if (usage != null) return self + ARGUMENT_SEPARATOR + usage
//            } else if (children.size > 1) {
//                val childUsage: MutableSet<String> = LinkedHashSet()
//                for (child in children) {
//                    val usage = getSmartUsage(child, source, childOptional, true)
//                    if (usage != null) childUsage.add(usage)
//                }
//                if (childUsage.size == 1) {
//                    val usage = childUsage.iterator().next()
//                    return self + ARGUMENT_SEPARATOR + if (childOptional) USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE else usage
//                } else if (childUsage.size > 1) {
//                    val builder = StringBuilder(open)
//                    var count = 0
//                    for (child in children) {
//                        if (count > 0) builder.append(USAGE_OR)
//                        builder.append(child.getUsageText())
//                        count++
//                    }
//                    if (count > 0) {
//                        builder.append(close)
//                        return self + ARGUMENT_SEPARATOR + builder.toString()
//                    }
//                }
//            }
//        }
//        return self
//    }
}
