package net.cjsah.bot.console.plugin;

import cc.moecraft.yaml.HyConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cjsah.bot.console.Files;
import net.cjsah.bot.console.util.Util;
import net.mamoe.mirai.Bot;
import org.hydev.logger.HyLogger;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class Plugin {
    /**
     * 插件名
     */
    public final String pluginName;
    /**
     * 插件版本
     */
    public final String pluginVersion;
    /**
     * 是否有配置文件
     */
    public final boolean hasConfig;
    /**
     * 插件作者
     * @see List#of(Object[]) 
     */
    public final List<String> pluginAuthors;
    /**
     * logger日志输出
     */
    public final HyLogger logger;
    /**
     * Bot
     */
    private Bot bot;

    public Plugin(String pluginName, String pluginVersion, boolean hasConfig, List<String> pluginAuthors) {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.hasConfig = hasConfig;
        this.pluginAuthors = pluginAuthors;
        this.logger = new HyLogger(Util.INSTANCE.getColor(204, 255, 51, true) + pluginName);
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    /**
     * 插件配置目录
     */
    public File getPluginDir() {
        return new File(Files.PLUGINS.getFile(), pluginName);
    }

    /**
     * 插件启动
     */
    public abstract void onPluginStart();

    /**
     * 插件关闭
     */
    public void onPluginStop() {}

    /**
     * 获取yml格式配置文件
     */
    protected HyConfig getYamlConfig(String name, Consumer<HyConfig> defaultValue) {
        return Util.INSTANCE.getYaml(new File(getPluginDir(), name), defaultValue);
    }

    /**
     * 获取json格式配置文件
     */
    protected JsonElement getJsonConfig(String name, Consumer<JsonObject> defaultValue) {
        return Util.INSTANCE.getJson(new File(getPluginDir(), name), defaultValue);
    }

}
