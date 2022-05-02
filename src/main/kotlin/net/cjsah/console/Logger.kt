@file:Suppress("unused")

package net.cjsah.console


import net.cjsah.console.text.Text
import org.apache.logging.log4j.LogManager

object Logger {
    @JvmStatic
    private val logger = LogManager.getLogger(Language.translate("console.name"))

    @JvmStatic
    fun debug(text: Text) {
        logger.debug(text.toString())
    }

    @JvmStatic
    fun error(text: Text) {
        logger.error(text.toString())
    }

    @JvmStatic
    fun fatal(text: Text) {
        logger.fatal(text.toString())
    }

    @JvmStatic
    fun info(text: Text) {
        logger.info(text.toString())
    }

    @JvmStatic
    fun trace(text: Text) {
        logger.trace(text.toString())
    }

    @JvmStatic
    fun warn(text: Text) {
        logger.warn(text.toString())
    }

    @JvmStatic
    fun debug(message: String) {
        logger.debug(message)
    }

    @JvmStatic
    fun error(message: String) {
        logger.error(message)
    }

    @JvmStatic
    fun fatal(message: String) {
        logger.fatal(message)
    }

    @JvmStatic
    fun info(message: String) {
        logger.info(message)
    }

    @JvmStatic
    fun trace(message: String) {
        logger.trace(message)
    }

    @JvmStatic
    fun warn(message: String) {
        logger.warn(message)
    }

}