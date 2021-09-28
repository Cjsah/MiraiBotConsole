package net.cjsah.console.plugin;

import cc.moecraft.yaml.HyConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cjsah.console.Console;
import net.cjsah.console.ConsoleFiles;
import net.cjsah.console.Util;
import net.cjsah.console.exceptions.PluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.function.Consumer;

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
    public abstract void onPluginLoad();

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

    /**
     * 获取yml格式配置文件
     * @param name 配置文件名
     * @param defaultValue 默认值
     * @return 配置文件
     */
    protected HyConfig getYamlConfig(String name, Consumer<HyConfig> defaultValue) {
        return Util.INSTANCE.getYaml(new File(getPluginDir(), name), defaultValue);
    }

    /**
     * 获取json格式配置文件
     * @param name 配置文件名
     * @param defaultValue 默认值
     * @return 配置文件
     */
    protected JsonElement getJsonConfig(String name, Consumer<JsonObject> defaultValue) {
        return Util.INSTANCE.getJson(new File(getPluginDir(), name), defaultValue);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) v%s", this.info.getName(), this.info.getId(), this.info.getVersion());
    }
}
