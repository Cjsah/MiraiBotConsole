package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import java.lang.Exception

internal class AccountConfig : HyConfig(Files.ACCOUNT.file, false, true) {
    override fun save(): Boolean {
        return try {
            save(this.configFile)
            true
        } catch (e: Exception) {
            false
        }
    }
}