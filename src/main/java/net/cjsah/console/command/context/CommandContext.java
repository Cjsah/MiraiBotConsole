package net.cjsah.console.command.context;

import net.cjsah.console.command.Command;
import net.cjsah.console.command.source.CommandSource;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final Map<String, ParsedNodeResult<?>> arguments;
    private final CommandSource<?> source;
    private final Command command;

    public CommandContext(final Map<String, ParsedNodeResult<?>> arguments, final CommandSource<?> source, final Command command) {
        this.arguments = arguments;
        this.source = source;
        this.command = command;
    }

    @SuppressWarnings("unchecked")
    public <V> V getArgument(String name, Class<V> clazz) {
        ParsedNodeResult<?> argument;
        if (this.arguments.containsKey(name)) argument = this.arguments.get(name);
        else throw new IllegalArgumentException("此命令中不存在参数 '" + name + "'");
        Object result = argument.getResult();
        if (ClassConversion.get(clazz).isAssignableFrom(result.getClass())) {
            return (V) result;
        }else {
            throw new IllegalArgumentException("参数 '" + name + "' 被定义为 " + result.getClass().getSimpleName() + ", 而不是 " + clazz);
        }
    }

    public CommandSource<?> getSource() {
        return source;
    }

    public Command getCommand() {
        return command;
    }

    static class ClassConversion {
        private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

        static {
            PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
            PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
            PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
            PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
            PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
            PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
            PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
            PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
        }

        public static Class<?> get(Class<?> clazz) {
            return PRIMITIVE_TO_WRAPPER.getOrDefault(clazz , clazz);
        }

    }
}
