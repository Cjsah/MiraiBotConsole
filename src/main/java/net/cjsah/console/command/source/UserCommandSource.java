package net.cjsah.console.command.source;

import net.cjsah.console.Permission;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.util.Util;
import net.mamoe.mirai.contact.User;
import org.hydev.logger.LogLevel;

public class UserCommandSource extends CommandSource<User>{

    public UserCommandSource(User source) {
        super(source);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return permission.getLevel() <= Util.INSTANCE.getPermission(this.source).getLevel();
    }

    @Override
    public void sendFeedBack(String message) {
        this.source.sendMessage(message);
    }

    @Override
    public void sendFeedBack(String message, LogLevel level) throws CommandException {
        throw BuiltExceptions.commandSendByMember().create();
    }
}
