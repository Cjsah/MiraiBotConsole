package net.cjsah.console

import java.io.File

enum class Files(file: String, val isDirectory: Boolean) {
    ACCOUNT("account.yml", false),
    PLUGINS("plugins", true),
    IMAGES("images", true);

    val file: File = File(file)
}