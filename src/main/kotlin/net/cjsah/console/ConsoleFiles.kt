package net.cjsah.console

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.File
import java.util.function.Consumer

internal enum class ConsoleFiles(file: String, private val isDirectory: Boolean, private val initialize: Consumer<ConsoleFiles>?) {
    PERMISSIONS("permissions.json", false, {
        it.file.writeText(Util.GSON.toJson(JsonObject().apply {
            this.add("person", JsonObject().apply {
                this.add("owner", JsonArray())
                this.add("admin", JsonArray())
                this.add("helper", JsonArray())
            })
        }))
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
