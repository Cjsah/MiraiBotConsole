package net.cjsah.console.command.source;

import net.cjsah.console.Console;
import net.cjsah.console.Permission;
import org.hydev.logger.LogLevel;

public class ConsoleCommandSource extends CommandSource<Console> {

    public ConsoleCommandSource(Console source) {
        super(source);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return true;
    }

    @Override
    public void sendFeedBack(String message) {
        this.sendFeedBack(message, LogLevel.LOG);
    }

    @Override
    public void sendFeedBack(String message, LogLevel level) {
        switch (level) {
            case LOG -> Console.INSTANCE.getLogger().log(message);
            case WARNING -> Console.INSTANCE.getLogger().warning(message);
            case ERROR -> Console.INSTANCE.getLogger().error(message);
            case DEBUG -> Console.INSTANCE.getLogger().debug(message);
        }
    }
}
