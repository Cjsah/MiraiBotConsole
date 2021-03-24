package net.cjsah.console

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@Suppress("unused")
object Util {
    /**
     * json解析器
     *
     * @see Gson
     */
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * 文件下载
     */
    fun download(url: String, file: File) = thread(name = "BotDownloadService") {
        if (!file.exists()) {
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
    }
}