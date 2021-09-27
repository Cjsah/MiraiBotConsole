package net.cjsah.console

import cc.moecraft.yaml.HyConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.mamoe.mirai.contact.User
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
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

}