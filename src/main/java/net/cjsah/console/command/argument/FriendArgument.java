package net.cjsah.console.command.argument;

import net.cjsah.console.Console;
import net.cjsah.console.command.StringReader;
import net.cjsah.console.command.context.CommandContext;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.mamoe.mirai.contact.Friend;

public class FriendArgument implements Argument<Friend> {

    private FriendArgument() {
    }

    private static FriendArgument friend() {
        return new FriendArgument();
    }

    private static Friend getFriend(CommandContext context, String name) {
        return context.getArgument(name, Friend.class);
    }

    @Override
    public Friend parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        long id = reader.readLong();
        Friend friend = Console.INSTANCE.getBot().getFriend(id);
        if (friend == null) {
            reader.setCursor(start);
            throw BuiltExceptions.friendNotFound().createWithContext(reader, id);
        }
        return friend;
    }
}
