package net.cjsah.console.command.source;

import net.cjsah.console.Permission;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.Util;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import org.apache.logging.log4j.Level;

public class GroupCommandSource extends CommandSource<Group>{
    private final Member sender;

    public GroupCommandSource(Group source, final Member sender) {
        super(source);
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return permission.getLevel() <= Util.INSTANCE.getPermission(this.source).getLevel();
    }

    public boolean memberHasPermission(Permission permission) {
        return permission.getLevel() <= Util.INSTANCE.getPermission(this.sender).getLevel();
    }

    @Override
    public void sendFeedBack(String message) {
        this.source.sendMessage(message);
    }

    @Override
    public void sendFeedBack(String message, Level level) throws CommandException {
        throw BuiltExceptions.commandSendByMember().create();
    }
}
