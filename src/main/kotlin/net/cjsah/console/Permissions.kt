package net.cjsah.console

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.cjsah.console.plugin.Plugin
import net.mamoe.mirai.contact.ContactList
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group

class Permissions {
    private val list = HashMap<String, WBList>()
    private val permissions = HashMap<PermissionType, List<Long>>()

    fun init() {
        load()
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
        if (!list.containsKey(plugin.info.id)) list[plugin.info.id] = WBList(false, listOf(), listOf(), listOf(), listOf())
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

    fun getAllowGroup(plugin: Plugin): List<Group> {
        val wb = list[plugin.info.id]
        return if (wb == null) Console.getBot().groups.toList()
        else Console.getBot().groups.filter {
            if (wb.isWhite) wb.WG.contains(it.id)
            else !wb.BG.contains(it.id)
        }
    }

    fun getAllowUser(plugin: Plugin): List<Friend> {
        val wb = list[plugin.info.id]
        return if (wb == null) Console.getBot().friends.toList()
        else Console.getBot().friends.filter {
            if (wb.isWhite) wb.WU.contains(it.id)
            else !wb.BU.contains(it.id)
        }
    }

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

    private class WBList(
        var isWhite: Boolean,
        var WU: List<Long>,
        var WG: List<Long>,
        var BU: List<Long>,
        var BG: List<Long>
    ) {

        fun setListType(white: Boolean): Boolean {
            if (isWhite == white) return false
            isWhite = white
            return true
        }

        fun addWU(value: Long): Boolean {
            if (WU.contains(value)) return false
            WU = WU.plus(value)
            return true
        }
        fun addWG(value: Long): Boolean {
            if (WG.contains(value)) return false
            WG = WG.plus(value)
            return true
        }
        fun addBU(value: Long): Boolean {
            if (BU.contains(value)) return false
            BU = BU.plus(value)
            return true
        }
        fun addBG(value: Long): Boolean {
            if (BG.contains(value)) return false
            BG = BG.plus(value)
            return true
        }

        fun removeWU(value: Long): Boolean {
            if (!WU.contains(value)) return false
            WU = WU.filter{ it != value }
            return true
        }
        fun removeWG(value: Long): Boolean {
            if (!WU.contains(value)) return false
            WG = WG.filter{ it != value }
            return true
        }
        fun removeBU(value: Long): Boolean {
            if (!WU.contains(value)) return false
            BU = BU.filter{ it != value }
            return true
        }
        fun removeBG(value: Long): Boolean {
            if (!WU.contains(value)) return false
            BG = BG.filter{ it != value }
            return true
        }

    }

}