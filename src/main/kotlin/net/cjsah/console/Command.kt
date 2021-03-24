package net.cjsah.console

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface Command {
    suspend fun command()

    fun run() {
        GlobalScope.launch { command() }
    }
}