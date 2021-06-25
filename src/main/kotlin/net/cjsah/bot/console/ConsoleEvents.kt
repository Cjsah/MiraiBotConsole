package net.cjsah.bot.console

import net.cjsah.bot.console.command.CommandManager
import net.cjsah.bot.console.command.source.GroupCommandSource
import net.cjsah.bot.console.command.source.UserCommandSource
import net.cjsah.bot.console.util.Util
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import org.hydev.logger.HyLogger
import org.hydev.logger.format.AnsiColor
import java.io.File
import java.time.LocalDateTime

object ConsoleEvents {
    fun register(bot: Bot) {
        // command
        GlobalEventChannel.subscribeAlways<MessageEvent> {
            val msg = this.message.contentToString()
            if (msg.startsWith("/")) {
                if (this.subject is Group) CommandManager.execute(msg.substring(1, msg.length), GroupCommandSource(this.subject as Group, this.sender as Member))
                else if (this.subject is User) CommandManager.execute(msg.substring(1, msg.length), UserCommandSource(this.sender))
            }
        }
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
        HyLogger(prefix).log(changeContent)
    }

    private suspend fun downloadImageFile(image: Image): String {
        val date = LocalDateTime.now().toLocalDate()
        val imageDirection = File(Files.IMAGES.file, "${date.year}-${if (date.monthValue < 10) "0" else ""}${date.monthValue}")
        val imageFile = image.imageId.replace("""[{}]""".toRegex(), "")
        if (!imageDirection.exists()) imageDirection.mkdirs()
        Util.download(image.queryUrl(), File(imageDirection, imageFile))
        return imageFile
    }

}