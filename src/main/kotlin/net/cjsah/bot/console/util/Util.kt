package net.cjsah.bot.console.util

import cc.moecraft.yaml.HyConfig
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.cjsah.bot.console.Console
import net.cjsah.bot.console.Files
import net.cjsah.bot.console.Permission
import net.mamoe.mirai.contact.User
import org.hydev.logger.background
import org.hydev.logger.foreground
import java.awt.Color
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
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

    fun getColor(r: Int, g: Int, b: Int, foreground: Boolean): String {
        val color = Color(r, g, b)
        return if (foreground) color.foreground() else color.background()
    }


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
                Console.permissions.get(it.name.toLowerCase()).asJsonArray.forEach { id ->
                    if (id.asLong == user.id) return it
                }
            }
        }
        return Permission.USER
    }

    fun setPermission(id: Long, permission: Permission) {
        val json = Console.permissions

        if (permission != Permission.USER) {
            json.get(permission.name.toLowerCase()).asJsonArray.forEach { jsonID ->
                if (jsonID.asLong == id) {
                    Console.logger.warning("$id 已经是此权限, 无需修改")
                    return
                }
            }
        }else {
            var notInJson = true
            Permission.values().forEach each@{
                if (it != Permission.USER) {
                    json.get(it.name.toLowerCase()).asJsonArray.forEach { jsonID ->
                        if (jsonID.asLong == id) {
                            notInJson = false
                            return@each
                        }
                    }
                }
            }
            if (notInJson) {
                Console.logger.warning("$id 已经是此权限, 无需修改")
                return
            }
        }

        Permission.values().forEach {
            if (it != permission && it != Permission.USER) {
                json.get(it.name.toLowerCase()).asJsonArray.removeAll { jsonID -> jsonID.asLong == id }
            }
        }

        if (permission != Permission.USER) {
            json.get(permission.name.toLowerCase()).asJsonArray.add(id)
        }
        Files.PERMISSIONS.file.writeText(GSON.toJson(json))
        Console.logger.log("已将 $id 设为 ${permission.name.toLowerCase()}")
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
        if (file.exists()) return GSON.fromJson(file.readText())
        val json = JsonObject()
        default.accept(json)
        file.writeText(GSON.toJson(json))
        return json
    }

}