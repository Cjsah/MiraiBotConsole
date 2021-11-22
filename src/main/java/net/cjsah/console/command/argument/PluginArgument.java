package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.plugin.Plugin;
import net.cjsah.console.plugin.PluginLoader;

public class PluginArgument implements Argument<Plugin>{

    private PluginArgument() {
    }

    public static PluginArgument plugin() {
        return new PluginArgument();
    }

    public static Plugin getPlugin(CommandContext context, String name) {
        return context.getArgument(name, Plugin.class);
    }

    @Override
    public Plugin parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        String id = reader.readUnquotedString();
        Plugin plugin = PluginLoader.getPlugin(id);
        if (plugin == null) {
            reader.setCursor(start);
            throw BuiltExceptions.pluginNotFound().createWithContext(reader, id);
        }
        return plugin;
    }
}
