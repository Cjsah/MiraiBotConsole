package net.cjsah.console.command.source;

import net.cjsah.console.Permissions;
import net.cjsah.console.exceptions.BuiltExceptions;
import net.cjsah.console.exceptions.CommandException;
import net.cjsah.console.Util;
import net.cjsah.console.plugin.Plugin;
import net.mamoe.mirai.contact.User;
import org.apache.logging.log4j.Level;

public class UserCommandSource extends CommandSource<User>{

    public UserCommandSource(User source) {
        super(source);
    }

    @Override
    public boolean hasPermission(Permissions.PermissionType permission) {
        return Util.hasPermission(this.source, permission);
    }

    @Override
    public boolean CanUse(Plugin plugin) {
        return Util.canUse(plugin, this.source.getId(), true);
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
