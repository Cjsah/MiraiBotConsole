package net.cjsah.console

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object BotThread {
    private var running = false

    fun run() {
        this.running = true
        GlobalScope.launch {
            while (running) {

                delay(500)
            }
        }
    }

    fun stop() {
        this.running = false
    }
}