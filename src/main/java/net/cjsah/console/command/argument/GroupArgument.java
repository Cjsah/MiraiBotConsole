package net.cjsah.console.command.argument;

import net.cjsah.console.Console;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

public class GroupArgument implements Argument<Group> {

    private GroupArgument() {
    }

    private static GroupArgument group() {
        return new GroupArgument();
    }

    private static Group getGroup(CommandContext context, String name) {
        return context.getArgument(name, Group.class);
    }

    @Override
    public Group parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        long id = reader.readLong();
        Group group = Console.INSTANCE.getBot().getGroup(id);
        if (group == null) {
            reader.setCursor(start);
            throw BuiltExceptions.groupNotFound().createWithContext(reader, id);
        }
        return group;
    }
}
