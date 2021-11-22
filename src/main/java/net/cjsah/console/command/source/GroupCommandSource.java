package net.cjsah.console.command.source;

import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.Util;
import net.cjsah.console.plugin.Plugin;
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
    public boolean CanUse(Plugin plugin) {
        return Util.INSTANCE.canUse(plugin, this.source.getId(), false);
    }

    public boolean memberHasPermission(Plugin plugin) {
        return Util.INSTANCE.canUse(plugin, this.sender.getId(), true);
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
