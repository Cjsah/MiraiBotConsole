package net.cjsah.bot.console

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Suppress("all")
class RunEnvironment {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) { runBlocking { launch { main() } } }
    }
}