package net.cjsah.console

import cc.moecraft.yaml.HyConfig

internal class Account private constructor() : HyConfig(ConsoleFiles.ACCOUNT.file, false, true) {

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