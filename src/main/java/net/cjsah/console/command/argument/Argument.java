package net.cjsah.console.command.argument;

import net.cjsah.console.command.StringReader;
import net.cjsah.console.exceptions.CommandException;

public interface Argument<T> {
    T parse(StringReader reader) throws CommandException;
}
