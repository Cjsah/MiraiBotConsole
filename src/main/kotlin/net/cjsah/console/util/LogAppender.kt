package net.cjsah.console.util

import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.LogLevel
import org.hydev.logger.appenders.Appender
import org.hydev.logger.format.AnsiColor
import org.hydev.logger.now
import org.hydev.logger.parseFormats

class LogAppender : Appender() {
    init
    {
        // Create formatter
        val format = "&f[&5%s&f] [&1%s&f] [%s&f] %s&r".parseFormats()

        formatter =
            {
                val time = HyLoggerConfig.timePattern.now()

                when (it.level)
                {
                    LogLevel.LOG -> format.format(time, it.prefix, "${AnsiColor.GREEN}INFO", "${AnsiColor.RESET}${it.msg}")
                    LogLevel.WARNING -> format.format(time, it.prefix, "${AnsiColor.RED}WARNING", "${AnsiColor.YELLOW}${it.msg}")
                    LogLevel.DEBUG -> format.format(time, it.prefix, "${AnsiColor.CYAN}DEBUG", "${AnsiColor.CYAN}${it.msg}")
                    LogLevel.ERROR -> format.format(time, it.prefix, "${AnsiColor.RED}ERROR", "${AnsiColor.RED}${it.msg}")
                }
            }
    }

    override fun logRaw(message: String) = HyLoggerConfig.colorCompatibility.log(message)
}