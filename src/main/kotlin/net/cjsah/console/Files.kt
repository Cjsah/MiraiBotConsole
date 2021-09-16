package net.cjsah.console

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.cjsah.console.util.Util
import java.io.File
import java.util.function.Consumer

internal enum class Files(file: String, private val isDirectory: Boolean, private val initialize: Consumer<Files>?) {
    PERMISSIONS("permissions.json", false, {
        val json = JsonObject()
        json.add("owner", JsonArray())
        json.add("admin", JsonArray())
        json.add("helper", JsonArray())
        it.file.writeText(Util.GSON.toJson(json))
    }),
    ACCOUNT("account.yml", false, {
        with(Account.get()) {
            set("account", "QQ")
            set("password", "密码")
            save()
            return@with
        }
    }),
    PLUGINS("plugins", true, null),
    IMAGES("images", true, null);

    val file: File = File(file)

    fun create(): Boolean {
        if (this.file.exists()) return false
        if (isDirectory) this.file.mkdirs()
        else this.file.createNewFile()
        return true
    }

    companion object {
        internal fun init(): Boolean {
            var init = false
            values().forEach {
                if (it.create()) {
                    if (it == ACCOUNT) init = true
                    it.initialize?.accept(it)
                }
            }
            Account.get().load()
            return init
        }
    }
}
