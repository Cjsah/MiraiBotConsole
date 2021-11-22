package net.cjsah.console.command.source;

import net.cjsah.console.Console;
import net.cjsah.console.plugin.Plugin;
import org.apache.logging.log4j.Level;

public class ConsoleCommandSource extends CommandSource<Console> {

    public ConsoleCommandSource(Console source) {
        super(source);
    }

    @Override
    public boolean CanUse(Plugin plugin) {
        return true;
    }

    @Override
    public void sendFeedBack(String message) {
        this.sendFeedBack(message, Level.INFO);
    }

    @Override
    public void sendFeedBack(String message, Level level) {
        if (Level.INFO.equals(level)) {
            Console.INSTANCE.getLogger().info(message);
        } else if (Level.WARN.equals(level)) {
            Console.INSTANCE.getLogger().warn(message);
        } else if (Level.ERROR.equals(level)) {
            Console.INSTANCE.getLogger().error(message);
        } else if (Level.DEBUG.equals(level)) {
            Console.INSTANCE.getLogger().debug(message);
        }
    }
}
