package net.cjsah.console

import cc.moecraft.yaml.HyConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
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

    fun hasPermission(contact: Contact, permission: Permissions.PermissionType): Boolean {
        return getPermission(contact.id).ordinal >= permission.ordinal
    }

    fun getPermission(id: Long): Permissions.PermissionType {
        Permissions.PermissionType.values().forEach {
            if (it != Permissions.PermissionType.USER) {
                Console.permissions.getPermissionList(it).forEach { value ->
                    if (value == id) return it
                }
            }
        }
        return Permissions.PermissionType.USER
    }

    fun canUse(plugin: Plugin, id: Long, isUser: Boolean): Boolean {
        val whitelist = Console.permissions.isWhite(plugin)
        val list: List<Long> = if (whitelist) {
            if (isUser) Console.permissions.getWU(plugin)
            else Console.permissions.getWG(plugin)
        }else if (isUser) Console.permissions.getBU(plugin)
        else Console.permissions.getBG(plugin)
        list.forEach { if (it == id) return whitelist }
        return !whitelist
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
        return jsonArray.map { it.asByte }
    }

    fun jsonArray2IntList(jsonArray: JsonArray): List<Int> {
        return jsonArray.map { it.asInt }
    }

    fun jsonArray2ShortList(jsonArray: JsonArray): List<Short> {
        return jsonArray.map { it.asShort }
    }

    fun jsonArray2LongList(jsonArray: JsonArray): List<Long> {
        return jsonArray.map { it.asLong }
    }

    fun jsonArray2BigDecimalList(jsonArray: JsonArray): List<BigDecimal> {
        return jsonArray.map { it.asBigDecimal }
    }

    fun jsonArray2BigIntegerList(jsonArray: JsonArray): List<BigInteger> {
        return jsonArray.map { it.asBigInteger }
    }

    fun jsonArray2FloatList(jsonArray: JsonArray): List<Float> {
        return jsonArray.map { it.asFloat }
    }

    fun jsonArray2DoubleList(jsonArray: JsonArray): List<Double> {
        return jsonArray.map { it.asDouble }
    }

    fun jsonArray2BooleanList(jsonArray: JsonArray): List<Boolean> {
        return jsonArray.map { it.asBoolean }
    }

    fun jsonArray2StringList(jsonArray: JsonArray): List<String> {
        return jsonArray.map { it.asString }
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