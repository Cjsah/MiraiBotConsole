package net.cjsah.bot.console

import cc.moecraft.yaml.HyConfig
import com.google.common.collect.Lists
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol.ANDROID_PAD
import org.hydev.logger.HyLogger
import org.hydev.logger.format.AnsiColor
import java.io.File
import java.lang.Exception
import java.lang.NullPointerException
import java.net.URL
import java.net.URLClassLoader
import java.time.LocalDateTime
import java.util.jar.JarFile

@Suppress("unused")
object Console {
    lateinit var bot: Bot
    var stopConsole = false
    val logger = HyLogger("控制台")
    private val loadingPlugins = mutableListOf<Plugin>()

    private fun loadPlugin(plugin: Plugin) {
        GlobalScope.launch {
            plugin.bot = bot
            if (plugin.hasConfig && !plugin.pluginDir.exists()) plugin.pluginDir.mkdir()
            plugin.onPluginStart()
            loadingPlugins.add(plugin)
            logger.log("${plugin.pluginName} ${plugin.pluginVersion} 插件已启动!")
        }
    }

    private fun unloadPlugin(plugin: Plugin) {
        GlobalScope.launch {
            plugin.onPluginStop()
            loadingPlugins.remove(plugin)
            logger.log("${plugin.pluginName} 插件已关闭!")
        }
    }

    fun loadAllPlugins() {
        getPluginJars().forEach {
            getPlugin(it)?.let { plugin -> loadPlugin(plugin) }
        }
    }

    fun unloadAllPlugins() {
        loadingPlugins.forEach {
            unloadPlugin(it)
        }
    }
}

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

internal enum class Files(file: String, val isDirectory: Boolean) {
    ACCOUNT("account.yml", false),
    PLUGINS("plugins", true),
    IMAGES("images", true);

    val file: File = File(file)
}

suspend fun main() {
    val logger = Console.logger
    val config = AccountConfig()
    if (initFiles(config)) {
        logger.log("初始化完成")
        logger.log("请在${config.configFile.name}中填入你的QQ号和密码后重启Bot")
        return
    }

    config.load()
    logger.log("登录账号: ${config.getLong("account")}")

    Console.bot = BotFactory.newBot(config.getLong("account"), config.getString("password")) {
        fileBasedDeviceInfo("${config.getLong("account")}.json")
        noBotLog()
        noNetworkLog()
        enableContactCache()
        protocol = ANDROID_PAD
    }.alsoLogin()

    if (Console.bot.isOnline) logger.log("登录成功")
    registerEvents(Console.bot)
    logger.log("控制台初始化完成")
    logger.log("正在加载插件...")
    Console.loadAllPlugins()
    logger.log("插件加载完成")

    startListener()

    logger.log("控制台退出...")
}

private fun startListener() {
    Console.logger.log("控制台启动完成")
    while (!Console.stopConsole) ConsoleCommand.run(readLine())

    Console.unloadAllPlugins()

    Console.bot.close()
}

private fun registerEvents(bot: Bot) {
    GlobalEventChannel.subscribeAlways<FriendMessageEvent> {
        sendLogger(
            "${AnsiColor.GREEN}好友消息",
            "<${sender.id}> [${sender.nick}] ${message.contentToString()}",
            message
        )
    }
    GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
        sendLogger(
            "${AnsiColor.YELLOW}群组消息",
            "<${group.id}> [${sender.nameCardOrNick}] ${message.contentToString()}",
            message
        )
    }

    GlobalEventChannel.subscribeAlways<GroupTempMessageEvent> {
        sendLogger(
            "${AnsiColor.CYAN}临时消息",
            "<${sender.id}> [${sender.nameCardOrNick}] ${message.contentToString()}",
            message
        )
    }

    GlobalEventChannel.subscribeAlways<MemberJoinEvent> {
        sendLogger(
            "${AnsiColor.YELLOW}群组消息",
            "用户${this.member.id} 加入了群组"
        )
    }

    GlobalEventChannel.subscribeAlways<MemberLeaveEvent> {
        sendLogger(
            "${AnsiColor.YELLOW}群组消息",
            "用户${this.member.id} 离开了群组"
        )
    }

    GlobalEventChannel.subscribeAlways<GroupTempMessagePostSendEvent> {
        sendLogger(
            "${AnsiColor.CYAN}发送临时消息",
            "<${this.target.id}> [${this.target.nameCard}] ${message.contentToString()}"
        )
    }

    GlobalEventChannel.subscribeAlways<FriendMessagePostSendEvent> {
        sendLogger(
            "${AnsiColor.CYAN}发送好友消息",
            "<${this.target.id}> [${this.target.nick}] ${message.contentToString()}"
        )
    }

    GlobalEventChannel.subscribeAlways<GroupMessagePostSendEvent> {
        sendLogger(
            "${AnsiColor.CYAN}发送群组消息",
            "<${this.target.id}> [${bot.nick}] ${message.contentToString()}"
        )
    }

    GlobalEventChannel.subscribeAlways<BotOfflineEvent> {
        sendLogger(
            "QQ状态改变",
            "账号 ${this.bot.id} 已离线"
        )
    }

    GlobalEventChannel.subscribeAlways<BotReloginEvent> {
        sendLogger(
            "QQ状态改变",
            "账号 ${this.bot.id} 已重新登录"
        )
    }

}

private suspend fun sendLogger(prefix: String, content: String, messageChain: MessageChain? = null) {
    var image: String? = null
    var changeContent = content
    messageChain?.forEach {
        if (it is Image) image = downloadImageFile(it)
    }
    image?.let { changeContent = "$content [$image]" }
    HyLogger(prefix).log(changeContent )
}

private suspend fun downloadImageFile(image: Image): String {
    val date = LocalDateTime.now().toLocalDate()
    val imageDirection = File(Files.IMAGES.file, "${date.year}-${if (date.monthValue < 10) "0" else ""}${date.monthValue}")
    val imageFile = image.imageId.replace("""[{}]""".toRegex(), "")
    if (!imageDirection.exists()) imageDirection.mkdirs()
    Util.download(image.queryUrl(), File(imageDirection, imageFile))
    return imageFile
}

private fun initFiles(config: AccountConfig): Boolean {
    var init = false
    Files.values().forEach {
        if (it == Files.ACCOUNT && !it.file.exists()) {
            with(config) {
                it.file.createNewFile()
                set("account", "QQ")
                set("password", "密码")
                save()
                init = true
            }
        }
        if (!it.file.exists()) {
            if (it.isDirectory) it.file.mkdirs()
            else it.file.createNewFile()
        }
    }
    return init
}

private fun getPluginJars(): List<File> {
    val jars = Lists.newArrayList<File>()
    Files.PLUGINS.file.listFiles()?.forEach {
        if (it.isFile && it.name.endsWith(".jar")) jars.add(it)
    }
    return jars
}

private fun getPlugin(jar: File): Plugin? {
    return try {
        val ucl = URLClassLoader(arrayOf(URL("jar:${jar.toURI().toURL()}!/")))

        Class.forName(JarFile(jar).manifest.mainAttributes.getValue("Plugin-Class")!!, true, ucl)
            .getDeclaredConstructor().newInstance() as Plugin

    }catch (e: Exception) {
        val logger = Console.logger
        when (e) {
            is NullPointerException -> logger.warning("找不到插件 ${jar.name} 启动入口点, 请在插件中设置Plugin-Main启动入口")
            is ClassNotFoundException -> logger.warning("找不到插件 ${jar.name} 启动入口点, 请正确设置插件Plugin-Main启动入口")
            else -> logger.warning("插件 ${jar.name} 无法加载")
        }
        null
    }
}