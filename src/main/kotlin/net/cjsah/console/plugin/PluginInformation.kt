@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.cjsah.console.plugin

import com.google.gson.JsonObject
import net.cjsah.console.exceptions.PluginException

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
        if (!"""[a-z_][a-z\d_-]*""".toRegex().matches(id)) throw PluginException("插件id不符合命名规范!")
        this.information["contact"].asJsonObject.entrySet().forEach { (key, value) ->
            contacts[key] = value.asString
        }
        if (this.information.has("depends")) {
            this.information["depends"].asJsonObject.entrySet().forEach { (key, value) ->
                if (!"""[\[(][\d.]+,[\d.]+[])]""".toRegex().matches(key))
                    throw PluginException("插件 [${this.name} (${this.id}) v${this.version}] 中的依赖 $key 不符合版本格式规范 [\\[(][\\d.]+,[\\d.]+[\\])] ")
                depends[key] = value.asString
            }
        }
    }
}