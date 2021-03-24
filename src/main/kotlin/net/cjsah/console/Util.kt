package net.cjsah.console

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

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
    private val downloads = HashMap<String, File>()
    private val downloadThread = Thread {
        downloads.entries.removeIf {
            try {
                val huc = URL(it.key).openConnection() as HttpURLConnection
                huc.connect()
                huc.inputStream.use { input ->
                    BufferedOutputStream(FileOutputStream(it.value)).use { output ->
                        input.copyTo(output)
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
            true
        }
    }
    fun download(url: String, file: File) {
        downloads[url] = file
        if (!downloadThread.isAlive) downloadThread.start()
    }
}