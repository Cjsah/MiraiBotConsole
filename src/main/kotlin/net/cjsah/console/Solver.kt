package net.cjsah.console

import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver
import org.apache.logging.log4j.LogManager
import java.io.File

class Solver : LoginSolver() {
    private val logger = LogManager.getLogger("登陆")
    private val loginSolverLock = Mutex()

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? = loginSolverLock.withLock {
        @Suppress("BlockingMethodInNonBlockingContext")
        val tempFile: File = File.createTempFile("tmp", ".png").apply { deleteOnExit() }
        withContext(Dispatchers.IO) {
            tempFile.createNewFile()
            logger.info("需要图片验证码登录, 验证码为 4 字母")
            try {
                tempFile.writeChannel().apply {
                    writeFully(data)
                    close()
                }
                logger.info("请查看文件 ${tempFile.absolutePath}")
            } catch (e: Exception) {
                logger.warn("验证码文件保存失败", e)
            }
        }
        logger.info("请输入 4 位字母验证码. 若要更换验证码, 请直接回车")
        val context : String = readLine()!!
        return context.replace("\n", "").takeUnless { it.length != 4 || it.isEmpty() }.also {
            logger.info("正在提交 $it...")
        }
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        logger.info("需要滑动验证码, 请在任意浏览器中打开以下链接并完成验证码, 然后请按回车")
        logger.info("链接: $url" )

        return readLine().also {
            logger.info("正在提交中...")
        }

    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        logger.info("当前登录环境不安全，服务器要求账户认证。请在任意浏览器打开 $url 并完成验证后输入任意字符。")
        return readLine().also {
            logger.info("正在提交中...")
        }

    }
}
