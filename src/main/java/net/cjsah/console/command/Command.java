package net.cjsah.console.command;

import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.CommandException;

public interface Command {
    int SUCCESSFUL = 1;

    int run(CommandContext context) throws CommandException;
}
