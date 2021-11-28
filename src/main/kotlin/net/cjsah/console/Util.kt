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
    @JvmStatic
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * 文件下载
     */
    @JvmStatic
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
    @JvmStatic
    fun save(file: File, text: String) {
        file.writeText(text)
    }

    @JvmStatic
    fun fromJson(file: File): JsonObject {
        return GSON.fromJson(file.readText(), JsonObject::class.java)
    }

    @JvmStatic
    fun hasPermission(contact: Contact, permission: Permissions.PermissionType): Boolean {
        return getPermission(contact.id).ordinal >= permission.ordinal
    }

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
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

    @JvmStatic
    fun getJson(file: File, default: Consumer<JsonObject>): JsonElement {
        if (file.exists()) return GSON.fromJson(file.readText(), JsonObject::class.java)
        val json = JsonObject()
        default.accept(json)
        file.writeText(GSON.toJson(json))
        return json
    }

    @JvmStatic
    fun jsonArray2ByteList(jsonArray: JsonArray): List<Byte> {
        return jsonArray.map { it.asByte }
    }

    @JvmStatic
    fun jsonArray2IntList(jsonArray: JsonArray): List<Int> {
        return jsonArray.map { it.asInt }
    }

    @JvmStatic
    fun jsonArray2ShortList(jsonArray: JsonArray): List<Short> {
        return jsonArray.map { it.asShort }
    }

    @JvmStatic
    fun jsonArray2LongList(jsonArray: JsonArray): List<Long> {
        return jsonArray.map { it.asLong }
    }

    @JvmStatic
    fun jsonArray2BigDecimalList(jsonArray: JsonArray): List<BigDecimal> {
        return jsonArray.map { it.asBigDecimal }
    }

    @JvmStatic
    fun jsonArray2BigIntegerList(jsonArray: JsonArray): List<BigInteger> {
        return jsonArray.map { it.asBigInteger }
    }

    @JvmStatic
    fun jsonArray2FloatList(jsonArray: JsonArray): List<Float> {
        return jsonArray.map { it.asFloat }
    }

    @JvmStatic
    fun jsonArray2DoubleList(jsonArray: JsonArray): List<Double> {
        return jsonArray.map { it.asDouble }
    }

    @JvmStatic
    fun jsonArray2BooleanList(jsonArray: JsonArray): List<Boolean> {
        return jsonArray.map { it.asBoolean }
    }

    @JvmStatic
    fun jsonArray2StringList(jsonArray: JsonArray): List<String> {
        return jsonArray.map { it.asString }
    }

    @JvmStatic
    @JvmName("StringList2JsonArray")
    fun list2JsonArray(List: Collection<String>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmStatic
    @JvmName("NumberList2JsonArray")
    fun list2JsonArray(List: Collection<Number>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmStatic
    @JvmName("CharList2JsonArray")
    fun list2JsonArray(List: Collection<Char>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmStatic
    @JvmName("BooleanList2JsonArray")
    fun list2JsonArray(List: Collection<Boolean>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

    @JvmStatic
    @JvmName("JsonElementList2JsonArray")
    fun list2JsonArray(List: Collection<JsonElement>): JsonArray {
        val array = JsonArray()
        List.forEach { array.add(it) }
        return array
    }

}