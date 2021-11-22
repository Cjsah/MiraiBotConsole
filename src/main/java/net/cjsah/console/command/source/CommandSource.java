package net.cjsah.console.command.source;

import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.plugin.Plugin;
import org.apache.logging.log4j.Level;

public abstract class CommandSource<T> {
    protected final T source;

    protected CommandSource(final T source) {
        this.source = source;
    }

    public abstract boolean CanUse(Plugin plugin);

    public abstract void sendFeedBack(String message) throws CommandException;

    public abstract void sendFeedBack(String message, Level level) throws CommandException;

    public T getSource() {
        return this.source;
    }
}
