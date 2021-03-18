package net.cjsah.console

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.*
import net.cjsah.console.exceptions.JsonFileParameterException
import java.io.*

object Util {
    /**
     * json解析器
     *
     * @see Gson
     */
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * 从json文件获取json内容
     *
     * @param file [File] 文件地址
     * @return [JsonElement] 解析出来的json内容
     *
     * @see JsonElement
     */
    fun file2Json(file : File): JsonElement {
        if (!file.isFile || !file.name.endsWith(".json")) {
            throw JsonFileParameterException("Please pass in a json parameter")
        }else {
            return GSON.fromJson(file.readText())
        }
    }

    /**
     * 把json写入文件中
     *
     * @param json Json数据
     * @param file 要写入的文件
     */
    fun json2File(json: JsonElement, file: File) {
        file.writeText(GSON.toJson(json))
    }
}