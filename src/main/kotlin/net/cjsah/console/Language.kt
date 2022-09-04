package net.cjsah.console

import com.google.gson.JsonObject
import java.io.File
import java.net.JarURLConnection

object Language {
    @JvmStatic
    private var language: String = "zh_cn"
    @JvmStatic
    private val translate = mutableMapOf<String, MutableMap<String, String>>()

    @JvmStatic
    private fun decode(language: String, json: JsonObject) {
        val lang = language.lowercase()
        if (!translate.containsKey(lang)) {
            translate[lang] = mutableMapOf()
        }
        json.entrySet().forEach {
            translate[lang]!![it.key] = it.value.asString
        }
    }

    @JvmStatic
    fun translate(key: String): String {
        return translate[language]?.get(key) ?: key
    }

    @JvmStatic
    internal fun setLanguage(language: String) {
        this.language = language.lowercase()
    }

    @JvmStatic
    internal fun load() {
        Language.javaClass.classLoader.getResource("languages")?.let { url ->
            if (url.protocol == "jar") {
                (url.openConnection()as JarURLConnection).jarFile.entries().iterator().forEach {
                    if ("""languages/[a-zA-z\d_]+.json""".toRegex().matches(it.name)) {
                        val stream = Language.javaClass.classLoader.getResourceAsStream(it.name)!!
                        val json = Util.GSON.fromJson(stream.reader(), JsonObject::class.java)
                        decode(it.name.substring(10, it.name.length - 5), json)
                    }
                }
            } else if (url.protocol == "file") {
                File(url.path).listFiles()?.forEach {
                    if (it.name.endsWith(".json")) {
                        val json = Util.GSON.fromJson(it.readText(), JsonObject::class.java)
                        decode(it.name.substring(0, it.name.length - 5).lowercase(), json)
                    }
                }
            }
        }
    }
}