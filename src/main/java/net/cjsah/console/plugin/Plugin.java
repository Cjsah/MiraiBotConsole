package net.cjsah.console.plugin;

import net.cjsah.console.Console;
import net.cjsah.console.ConsoleFiles;
import net.cjsah.console.exceptions.PluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class Plugin {
    private PluginInformation info;
    private boolean init = false;
    protected Logger logger;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void init(final PluginInformation info) {
        if (!this.init) {
            this.info = info;
            this.logger = LogManager.getLogger(info.getName());
            if (info.hasConfig() && (!this.getPluginDir().exists() || this.getPluginDir().isDirectory())) {
                this.getPluginDir().mkdirs();
            }
            this.onPluginLoad();
            this.init = true;
        }else {
            throw new PluginException("插件已经初始化, 请勿再次初始化");
        }
    }

    public PluginInformation getInfo() {
        return this.info;
    }

    /**
     * 插件加载时执行 ({@link Console#bot} = null)
     */
    protected abstract void onPluginLoad();

    /**
     * 机器人登陆后执行
     */
    public abstract void onBotStarted();

    /**
     * 机器人下线后执行
     */
    public abstract void onBotStopped();

    /**
     * 插件卸载时执行
     */
    public abstract void onPluginUnload();

    /**
     * 获取插件配置目录
     * @return 配置文件夹
     */
    public File getPluginDir() {
        if (!this.init) throw new PluginException("插件还未初始化, 请先初始化");
        if (!this.info.hasConfig()) throw new PluginException("此插件没有配置文件");
        return new File(ConsoleFiles.PLUGINS.getFile(), this.info.getId());
    }

    @Override
    public String toString() {
        return String.format("%s (%s) v%s", this.info.getName(), this.info.getId(), this.info.getVersion());
    }
}
