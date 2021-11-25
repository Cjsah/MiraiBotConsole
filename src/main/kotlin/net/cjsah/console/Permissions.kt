package net.cjsah.console

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.cjsah.console.plugin.Plugin

class Permissions {
    private val list = HashMap<String, WBList>()
    private val permissions = HashMap<PermissionType, List<Long>>()

    init { load() }

    companion object {
        fun getEmpty() = WBList(false, listOf(), listOf(), listOf(), listOf())
    }

    fun reload() {
        list.clear()
        load()
    }

    private fun load() {
        Util.fromJson(ConsoleFiles.PERMISSIONS.file).entrySet().forEach { kv ->
            if (kv.key == "person") {
                permissions[PermissionType.OWNER] = kv.value.asJsonObject.get("owner").asJsonArray.map { it.asLong }
                permissions[PermissionType.ADMIN] = kv.value.asJsonObject.get("admin").asJsonArray.map { it.asLong }
                permissions[PermissionType.HELPER] = kv.value.asJsonObject.get("helper").asJsonArray.map { it.asLong }
            } else {
                list[kv.key] = WBList(
                    kv.value.asJsonObject.get("white").asBoolean,
                    kv.value.asJsonObject.get("whitelist").asJsonObject.get("user").asJsonArray.map { it.asLong },
                    kv.value.asJsonObject.get("whitelist").asJsonObject.get("group").asJsonArray.map { it.asLong },
                    kv.value.asJsonObject.get("blacklist").asJsonObject.get("user").asJsonArray.map { it.asLong },
                    kv.value.asJsonObject.get("blacklist").asJsonObject.get("group").asJsonArray.map { it.asLong }
                )
            }
        }
    }

    private fun save() {
        val json = JsonObject().apply {
            this.add("person", JsonObject().apply {
                permissions.entries.forEach { this.add(it.key.name.lowercase(), JsonArray().apply { it.value.forEach { value -> this.add(value) } }) }
                list.entries.forEach { this.add(it.key, JsonObject().apply {
                        this.addProperty("white", it.value.isWhite)
                        this.add("white-user", JsonArray().apply { it.value.WU.forEach { value -> this.add(value) } })
                        this.add("white-group", JsonArray().apply { it.value.WG.forEach { value -> this.add(value) } })
                        this.add("black-user", JsonArray().apply { it.value.BU.forEach { value -> this.add(value) } })
                        this.add("black-group", JsonArray().apply { it.value.BG.forEach { value -> this.add(value) } })
                    }) }
            })
        }

        ConsoleFiles.PERMISSIONS.file.writeText(Util.GSON.toJson(json))
    }

    fun addPlugin(plugin: Plugin) {
        if (!list.containsKey(plugin.info.id)) list[plugin.info.id] = getEmpty()
        save()
    }

    fun setListType(plugin: Plugin, white: Boolean): String {
        val type = list[plugin.info.id]?.setListType(white)
        save()
        return when(type) {
            true -> "已将插件 ${plugin.info.name} 切换为${if (white) "白" else "黑"}名单模式"
            false -> "插件 ${plugin.info.name} 已经是${if (white) "白" else "黑"}名单模式, 无需修改"
            else -> "插件 ${plugin.info.name} 不存在"
        }
    }

    fun addToList(plugin: Plugin, value: Long, isWhite: Boolean, isUser: Boolean): String {
        val type = if (isWhite) {
            if (isUser) list[plugin.info.id]?.addWU(value)
            else list[plugin.info.id]?.addWG(value)
        } else {
            if (isUser) list[plugin.info.id]?.addBU(value)
            else list[plugin.info.id]?.addBG(value)
        }
        save()
        return when(type) {
            true -> "已将${if (isUser) "用户" else "群"} $value 添加到插件 ${plugin.info.name} 的${if (isWhite) "白" else "黑"}名单"
            false -> "$value 已在其中, 无需修改"
            else -> "插件 ${plugin.info.name} 不存在"
        }
    }

    fun removeFromList(plugin: Plugin, value: Long, isWhite: Boolean, isUser: Boolean): String {
        val type = if (isWhite) {
            if (isUser) list[plugin.info.id]?.removeWU(value)
            else list[plugin.info.id]?.removeWG(value)
        } else {
            if (isUser) list[plugin.info.id]?.removeBU(value)
            else list[plugin.info.id]?.removeBG(value)
        }
        save()
        return when(type) {
            true -> "已将${if (isUser) "用户" else "群"} $value 从插件 ${plugin.info.name} ${if (isWhite) "白" else "黑"}名单中移除"
            false -> "$value 不在其中, 无需修改"
            else -> "插件 ${plugin.info.name} 不存在"
        }
    }

    fun getPermissionList(permission: PermissionType) = permissions[permission]!!

    fun isWhite(plugin: Plugin) = list[plugin.info.id]?.isWhite ?: false

    fun getWU(plugin: Plugin) = list[plugin.info.id]?.WU ?: listOf()

    fun getWG(plugin: Plugin) = list[plugin.info.id]?.WG ?: listOf()

    fun getBU(plugin: Plugin) = list[plugin.info.id]?.BU ?: listOf()

    fun getBG(plugin: Plugin) = list[plugin.info.id]?.BG ?: listOf()

    enum class PermissionType {
        USER,
        HELPER,
        ADMIN,
        OWNER
    }

    class WBList(
        internal var isWhite: Boolean,
        internal var WU: List<Long>,
        internal var WG: List<Long>,
        internal var BU: List<Long>,
        internal var BG: List<Long>
    ) {

        internal fun setListType(white: Boolean): Boolean {
            if (isWhite == white) return false
            isWhite = white
            return true
        }

        internal fun addWU(value: Long): Boolean {
            if (WU.contains(value)) return false
            WU = WU.plus(value)
            return true
        }
        internal fun addWG(value: Long): Boolean {
            if (WG.contains(value)) return false
            WG = WG.plus(value)
            return true
        }
        internal fun addBU(value: Long): Boolean {
            if (BU.contains(value)) return false
            BU = BU.plus(value)
            return true
        }
        internal fun addBG(value: Long): Boolean {
            if (BG.contains(value)) return false
            BG = BG.plus(value)
            return true
        }

        internal fun removeWU(value: Long): Boolean {
            if (!WU.contains(value)) return false
            WU = WU.filter{ it != value }
            return true
        }
        internal fun removeWG(value: Long): Boolean {
            if (!WU.contains(value)) return false
            WG = WG.filter{ it != value }
            return true
        }
        internal fun removeBU(value: Long): Boolean {
            if (!WU.contains(value)) return false
            BU = BU.filter{ it != value }
            return true
        }
        internal fun removeBG(value: Long): Boolean {
            if (!WU.contains(value)) return false
            BG = BG.filter{ it != value }
            return true
        }

    }

}