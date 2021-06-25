package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.cjsah.bot.console.util.Util

internal class Account private constructor() : HyConfig(Files.ACCOUNT.file, false, true) {

    companion object {
        private val account = Account()
        fun get() = account
    }

    override fun save(): Boolean {
        return try {
            save(this.configFile)
            true
        } catch (e: Exception) {
            false
        }
    }
}