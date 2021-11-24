package net.cjsah.console

import cc.moecraft.yaml.HyConfig
import com.google.gson.*
import net.cjsah.console.plugin.Plugin
import net.mamoe.mirai.contact.Contact
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.function.Consumer
import kotlin.concurrent.thread

@Suppress("unused")
object Util {
    /**
     * json解析器
     * @see Gson
     */
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * 文件下载
     */
    fun download(url: String, file: File) = thread(name = "BotDownloadService") {
        try {
            val huc = URL(url).openConnection() as HttpURLConnection
            huc.connect()
            huc.inputStream.use { input ->
                BufferedOutputStream(FileOutputStream(file)).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 给java用户使用
     */
    fun save(file: File, text: String) {
        file.writeText(text)
    }

    fun fromJson(file: File): JsonObject {
        return GSON.fromJson(file.readText(), JsonObject::class.java)
    }

    fun hasPermission(contact: Contact, permission: Permission): Boolean {
        return getPermission(contact.id).level >= permission.level
    }

    fun getPermission(id: Long): Permission {
        Permission.values().forEach {
            if (it != Permission.USER) {
                Console.permissions.get(it.name.lowercase(Locale.getDefault())).asJsonArray.forEach { je ->
                    if (je.asLong == id) return it
                }
            }
        }
        return Permission.USER
    }

    fun canUse(plugin: Plugin, id: Long, isUser: Boolean): Boolean {
        val permission = Console.permissions.get(plugin.info.id).asJsonObject
        val whitelist = permission.get("whitelist").asBoolean
        val list = permission.get(if (whitelist) "white" else "black").asJsonObject
            .get(if (isUser) "user" else "group").asJsonArray
        list.forEach { if (it.asLong == id) return whitelist }
        return !whitelist
    }

    fun setList(plugin: Plugin, id: Long, white: Boolean, isUser: Boolean): String {
        val list = Console.permissions.get(plugin.info.id).asJsonObject
            .get(if (white) "white" else "black").asJsonObject
            .get(if (isUser) "user" else "group").asJsonArray
        list.forEach {
            if (it.asLong == id) {
                return "$id 已在其中, 无需修改"
            }
        }
        list.add(id)
        ConsoleFiles.PERMISSIONS.file.writeText(GSON.toJson(Console.permissions))
        return "已将${if (isUser) "用户" else "群"} $id 添加到${if (white) "白名单" else "黑名单"}"
    }

    fun removeList(plugin: Plugin, id: Long, white: Boolean, isUser: Boolean): String {
        val list = Console.permissions.get(plugin.info.id).asJsonObject
            .get(if (white) "white" else "black").asJsonObject
            .get(if (isUser) "user" else "group").asJsonArray
        var value: JsonElement? = null
        list.forEach {
            if (it.asLong == id) {
                value = it
                return@forEach
            }
        }
        if (value == null) return "$id 不在其中, 无需修改"
        list.remove(value)
        ConsoleFiles.PERMISSIONS.file.writeText(GSON.toJson(Console.permissions))
        return "已将${if (isUser) "用户" else "群"} $id 移出${if (white) "白名单" else "黑名单"}"
    }

    fun getYaml(file: File, default: Consumer<HyConfig>): HyConfig {
        val config = HyConfig(file, false, true)
        if (!file.exists()) {
            config.let {
                default.accept(it)
                it.save()
            }
        }
        config.load()
        return config
    }

    fun getJson(file: File, default: Consumer<JsonObject>): JsonElement {
        if (file.exists()) return GSON.fromJson(file.readText(), JsonObject::class.java)
        val json = JsonObject()
        default.accept(json)
        file.writeText(GSON.toJson(json))
        return json
    }

    fun jsonArray2ByteList(jsonArray: JsonArray): List<Byte> {
        return jsonArray.map { it.asByte }.toList()
    }

    fun jsonArray2IntList(jsonArray: JsonArray): List<Int> {
        return jsonArray.map { it.asInt }.toList()
    }

    fun jsonArray2ShortList(jsonArray: JsonArray): List<Short> {
        return jsonArray.map { it.asShort }.toList()
    }

    fun jsonArray2LongList(jsonArray: JsonArray): List<Long> {
        return jsonArray.map { it.asLong }.toList()
    }

    fun jsonArray2BigDecimalList(jsonArray: JsonArray): List<BigDecimal> {
        return jsonArray.map { it.asBigDecimal }.toList()
    }

    fun jsonArray2BigIntegerList(jsonArray: JsonArray): List<BigInteger> {
        return jsonArray.map { it.asBigInteger }.toList()
    }

    fun jsonArray2FloatList(jsonArray: JsonArray): List<Float> {
        return jsonArray.map { it.asFloat }.toList()
    }

    fun jsonArray2DoubleList(jsonArray: JsonArray): List<Double> {
        return jsonArray.map { it.asDouble }.toList()
    }

    fun jsonArray2BooleanList(jsonArray: JsonArray): List<Boolean> {
        return jsonArray.map { it.asBoolean }.toList()
    }

    fun jsonArray2StringList(jsonArray: JsonArray): List<String> {
        return jsonArray.map { it.asString }.toList()
    }

    @JvmName("StringList2JsonArray")
    fun list2JsonArray(List: Collection<String>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmName("NumberList2JsonArray")
    fun list2JsonArray(List: Collection<Number>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmName("CharList2JsonArray")
    fun list2JsonArray(List: Collection<Char>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmName("BooleanList2JsonArray")
    fun list2JsonArray(List: Collection<Boolean>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmName("JsonElementList2JsonArray")
    fun list2JsonArray(List: Collection<JsonElement>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

}