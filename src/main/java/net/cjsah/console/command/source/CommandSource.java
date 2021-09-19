package net.cjsah.console.command.source;

import net.cjsah.console.Permission;
import net.cjsah.console.exceptions.CommandException;
import org.hydev.logger.LogLevel;

public abstract class CommandSource<T> {
    protected final T source;

    protected CommandSource(final T source) {
        this.source = source;
    }

    public abstract boolean hasPermission(Permission permission);

    public abstract void sendFeedBack(String message) throws CommandException;

    public abstract void sendFeedBack(String message, LogLevel level) throws CommandException;

    public T getSource() {
        return this.source;
    }
}
