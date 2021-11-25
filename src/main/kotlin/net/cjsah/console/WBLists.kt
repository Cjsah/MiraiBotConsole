package net.cjsah.console

import com.google.gson.JsonObject
import net.cjsah.console.plugin.Plugin

class WBLists(json: JsonObject) {
    private val list = HashMap<String, WBList>()

    init { load(json) }

    fun reload(json: JsonObject) {
        list.clear()
        load(json)
    }

    private fun load(json: JsonObject) {
        json.entrySet().forEach {
            if (it.key != "person") {
                list[it.key] = WBList(
                    Util.jsonArray2LongList(it.value.asJsonObject.get("white").asJsonObject.get("user").asJsonArray),
                    Util.jsonArray2LongList(it.value.asJsonObject.get("white").asJsonObject.get("group").asJsonArray),
                    Util.jsonArray2LongList(it.value.asJsonObject.get("black").asJsonObject.get("user").asJsonArray),
                    Util.jsonArray2LongList(it.value.asJsonObject.get("black").asJsonObject.get("group").asJsonArray)
                )
            }
        }
    }

    fun save() {

    }

    fun addNew(plugin: Plugin) {

    }

    fun setType(plugin: Plugin, white: Boolean) {

    }

    fun addToList(plugin: Plugin, value: Long, isWhite: Boolean, isUser: Boolean) {

    }

    fun removeFromList(plugin: Plugin, value: Long, isWhite: Boolean, isUser: Boolean) {
        val t: List<Long> = if (isWhite) {
            if (isUser) list[plugin.info.id]?.WU
            else list[plugin.info.id]?.WG
        } else {
            if (isUser) list[plugin.info.id]?.BU
            else list[plugin.info.id]?.BG
        } ?: listOf()
        t.filter{ it != value }


    }

    fun getWU(plugin: Plugin): List<Long> {
        return list[plugin.info.id]?.WU ?: listOf()
    }

    fun getWG(plugin: Plugin): List<Long> {
        return list[plugin.info.id]?.WG ?: listOf()
    }

    fun getBU(plugin: Plugin): List<Long> {
        return list[plugin.info.id]?.BU ?: listOf()
    }

    fun getBG(plugin: Plugin): List<Long> {
        return list[plugin.info.id]?.BG ?: listOf()
    }

    fun getPermission(level: Int): Permission {
        return Permission.values()[level]
    }

    enum class Permission {
        USER,
        HELPER,
        ADMIN,
        OWNER
    }

    private class WBList(var WU: List<Long>, var WG: List<Long>, var BU: List<Long>, var BG: List<Long>) {

    }

}