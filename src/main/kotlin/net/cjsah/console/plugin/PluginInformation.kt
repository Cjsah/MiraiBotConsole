@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.console.plugin

import com.google.gson.JsonObject
import net.cjsah.console.exceptions.ConsoleException
import net.cjsah.console.exceptions.PluginException
import net.cjsah.console.text.TranslateText

class PluginInformation(val information: JsonObject) {
    val contacts: MutableMap<String, String> = HashMap()
    val depends: MutableMap<String, String> = HashMap()
    val description: String = this.information["description"].asString
    val hasConfig: Boolean = this.information["hasConfig"].asBoolean
    val version: String = this.information["version"].asString
    val license: String = this.information["license"].asString
    val name: String = this.information["name"].asString
    val main: String = this.information["main"].asString
    val id: String = this.information["id"].asString
    val authors: List<String> = this.information["authors"].asJsonArray.map { it.asString }

    init {
        if (!"""[a-z_][a-z\d_-]*""".toRegex().matches(id))
            throw ConsoleException.create(TranslateText("plugin.convention"), PluginException::class.java)
        this.information["contact"].asJsonObject.entrySet().forEach { (key, value) ->
            contacts[key] = value.asString
        }
        if (this.information.has("depends")) {
            this.information["depends"].asJsonObject.entrySet().forEach { (key, value) ->
                if (!"""[\[(][\d.]+,[\d.]+[])]""".toRegex().matches(key))
                    throw ConsoleException.create(
                        TranslateText("plugin.convention.denpend", this.name, this.id, this.version, key),
                        PluginException::class.java
                    )
                depends[key] = value.asString
            }
        }
    }
}