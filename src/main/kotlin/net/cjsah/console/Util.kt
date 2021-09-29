package net.cjsah.console

import cc.moecraft.yaml.HyConfig
import com.google.gson.*
import net.mamoe.mirai.contact.User
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

    fun getPermission(user: User): Permission {
        Permission.values().forEach {
            if (it != Permission.USER) {
                Console.permissions.get(it.name.lowercase(Locale.getDefault())).asJsonArray.forEach { id ->
                    if (id.asLong == user.id) return it
                }
            }
        }
        return Permission.USER
    }

    fun setPermission(id: Long, permission: Permission) {
        val json = Console.permissions

        if (permission != Permission.USER) {
            json.get(permission.name.lowercase(Locale.getDefault())).asJsonArray.forEach { jsonID ->
                if (jsonID.asLong == id) {
                    Console.logger.warn("$id 已经是此权限, 无需修改")
                    return
                }
            }
        }else {
            var notInJson = true
            Permission.values().forEach each@{
                if (it != Permission.USER) {
                    json.get(it.name.lowercase(Locale.getDefault())).asJsonArray.forEach { jsonID ->
                        if (jsonID.asLong == id) {
                            notInJson = false
                            return@each
                        }
                    }
                }
            }
            if (notInJson) {
                Console.logger.warn("$id 已经是此权限, 无需修改")
                return
            }
        }

        Permission.values().forEach {
            if (it != permission && it != Permission.USER) {
                json.get(it.name.lowercase(Locale.getDefault())).asJsonArray.removeAll { jsonID -> jsonID.asLong == id }
            }
        }

        if (permission != Permission.USER) {
            json.get(permission.name.lowercase(Locale.getDefault())).asJsonArray.add(id)
        }
        ConsoleFiles.PERMISSIONS.file.writeText(GSON.toJson(json))
        Console.logger.info("已将 $id 设为 ${permission.name.lowercase(Locale.getDefault())}")
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