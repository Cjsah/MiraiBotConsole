package net.cjsah.console

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.cjsah.console.exceptions.JsonFileParameterException
import org.hydev.logger.HyLogger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class Util {
    /**
     * json解析器
     */
    val GSON : Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create()

    /**
     *
     */
    val LOGGER = HyLogger("MiraiBotConsole")

    /**
     * 从json文件获取json内容
     * @param file 文件地址
     * @return 解析出来的json内容
     */
    fun file2Json(file : File) {
        if (!file.isFile || !file.name.endsWith(".json")) {
            throw JsonFileParameterException("Please pass in a json parameter")
        }else {
            try {
                BufferedReader(FileReader(file)).use { reader ->
                    JSON.add(
                        file_name.substring(0, file_name.length - 5), GSON.fromJson<JsonObject>(
                            reader,
                            JsonObject::class.java
                        )
                    )
                }
            } catch (e: IOException) {
                logger.error("Failed to load config file $json_file")
                e.printStackTrace()
            }

        }

    }
}