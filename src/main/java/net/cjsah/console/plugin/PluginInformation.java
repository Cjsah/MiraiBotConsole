package net.cjsah.console.plugin;

import com.google.gson.JsonObject;
import net.cjsah.console.Util;
import net.cjsah.console.exceptions.PluginException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PluginInformation {
    private final JsonObject information;
    private final Map<String, String> contacts = new HashMap<>();
    private final Collection<String> authors;
    private final String description;
    private final boolean hasConfig;
    private final String version;
    private final String license;
    private final String name;
    private final String main;
    private final String id;

    public PluginInformation(final JsonObject information) {
        this.information = information;
        this.description = this.information.get("description").getAsString();
        this.hasConfig = this.information.get("hasConfig").getAsBoolean();
        this.version = this.information.get("version").getAsString();
        this.license = this.information.get("license").getAsString();
        this.name = this.information.get("name").getAsString();
        this.main = this.information.get("main").getAsString();
        this.id = this.information.get("id").getAsString();
        if (!Pattern.matches("[a-z_][a-z0-9_-]*", this.id)) throw new PluginException("插件id不符合命名规范!");
        this.authors = Util.jsonArray2StringList(this.information.get("authors").getAsJsonArray());
        this.information.get("contact").getAsJsonObject().entrySet().forEach((entry) -> this.contacts.put(entry.getKey(), entry.getValue().getAsString()));
    }

    @Deprecated
    public JsonObject getInformation() {
        return this.information;
    }

    public Map<String, String> getContacts() {
        return this.contacts;
    }

    public Collection<String> getAuthors() {
        return this.authors;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean hasConfig() {
        return this.hasConfig;
    }

    public String getVersion() {
        return this.version;
    }

    public String getLicense() {
        return this.license;
    }

    public String getName() {
        return this.name;
    }

    public String getMain() {
        return this.main;
    }

    public String getId() {
        return this.id;
    }
}
