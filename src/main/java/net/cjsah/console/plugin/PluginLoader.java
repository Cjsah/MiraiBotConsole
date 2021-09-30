package net.cjsah.console.plugin;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.cjsah.console.Console;
import net.cjsah.console.ConsoleFiles;
import net.cjsah.console.Util;
import net.cjsah.console.exceptions.PluginException;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginLoader {
    private static final Map<String, Plugin> MODS = new HashMap<>();
    private static int COUNT = 0;

    public static void onBotStarted() {
        MODS.values().forEach(Plugin::onBotStarted);
    }

    public static void onBotStopped() {
        MODS.values().forEach(Plugin::onBotStopped);
    }

    public static void onPluginUnload() {
        MODS.entrySet().removeIf((entry) -> {
            entry.getValue().onPluginUnload();
            return true;
        });

        MODS.values().forEach(Plugin::onPluginUnload);
    }

    public static void loadPlugins() throws Exception {
        Collection<File> jars = getPluginJars();
        COUNT = jars.size();
        Console.INSTANCE.getLogger().info((COUNT == 0) ? "没有插件被加载" : "正在加载 " + COUNT + " 个插件");
        for (File jar : jars) {
            Plugin plugin = getPlugin(jar);
            MODS.put(plugin.getInfo().getId(), plugin);
            Console.INSTANCE.getLogger().info(String.format("插件 %s %s 已加载", plugin.getInfo().getName(), plugin.getInfo().getVersion()));
        }
    }

    public static int getCount() {
        return COUNT;
    }

    private static Collection<File> getPluginJars() {
        File[] files = ConsoleFiles.PLUGINS.getFile().listFiles();
        if (files == null) return Collections.emptyList();
        return Arrays.stream(files).filter((file) -> file.isFile() && file.getName().endsWith(".jar") && !file.getName().startsWith(".") && !file.isHidden()).collect(Collectors.toList());
    }

    private static Plugin getPlugin(File file) throws Exception {
        URI uri = file.toURI();
        URI jarURI = new URI("jar:" + uri.getScheme(), uri.getHost(), uri.getPath(), uri.getFragment());
        FileSystem fileSystem = FileSystems.newFileSystem(jarURI, Maps.newHashMap());
        Path path = fileSystem.getPath("plugin.json");
        InputStream inputStream = Files.newInputStream(path);
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonObject json = Util.INSTANCE.getGSON().fromJson(reader, JsonObject.class);
        inputStream.close();
        reader.close();
        PluginInformation info = new PluginInformation(json);
        if (MODS.containsKey(info.getId())) throw new PluginException("插件 " + file + " [" + String.format("%s (%s) v%s", info.getName(), info.getId(), info.getVersion()) + "] 无法加载, 已有同名插件 " + MODS.get(info.getId()));
        String main = info.getMain();
        URLClassLoader ucl = new URLClassLoader(new URL[]{new URL("jar:" + uri.toURL() + "!/")});
        Class<?> clazz = Class.forName(main, true, ucl);
        Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
        plugin.init(info);
        return plugin;
    }

//    private static void loadClassPathPlugin() throws Exception {
//        if (isDevelopment()) {
//            ClassLoader loader = PluginLoader.class.getClassLoader();
//            InputStream resource = loader.getResourceAsStream("plugin.json");
//            InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8);
//            JsonObject json = Util.INSTANCE.getGSON().fromJson(reader, JsonObject.class);
//            resource.close();
//            reader.close();
//            PluginInformation info = new PluginInformation(json);
//            if (MODS.containsKey(info.getId())) throw new PluginException("插件 " + info.getName() + " [" + String.format("%s (%s) v%s", info.getName(), info.getId(), info.getVersion()) + "] 无法加载, 已有同名插件 " + MODS.get(info.getId()));
//            String main = info.getMain();
//            Class<?> clazz = Class.forName(main, true, loader);
//            Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
//            plugin.init(info);
//            MODS.put(plugin.getInfo().getId(), plugin);
//            Console.INSTANCE.getLogger().info(String.format("插件 %s %s 已加载", plugin.getInfo().getName(), plugin.getInfo().getVersion()));
//
//        }
//    }
//
//
}
