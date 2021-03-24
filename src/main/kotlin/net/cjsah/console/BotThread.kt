package net.cjsah.console

import java.lang.Thread.sleep
import kotlin.concurrent.thread

object BotThread {
    private lateinit var t: Thread
    private var stop = false

    fun run() {
        stop = false
        t = thread(isDaemon = true, name = "BotConsole", priority = 3) {
            while (!stop) {

                sleep(1000)
            }
        }
    }

    fun stop() {
        stop = true
    }
}