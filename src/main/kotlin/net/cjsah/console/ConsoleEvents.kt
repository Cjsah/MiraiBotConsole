package net.cjsah.console

import net.cjsah.console.command.CommandManager
import net.cjsah.console.command.source.GroupCommandSource
import net.cjsah.console.command.source.UserCommandSource
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.apache.logging.log4j.LogManager
import java.io.File
import java.time.LocalDateTime

internal object ConsoleEvents {
    fun register() {
        // command
        GlobalEventChannel.subscribeAlways<MessageEvent> {
            val msg = this.message.contentToString()
            if (msg.startsWith("/")) {
                if (this.subject is Group) CommandManager.execute(msg.substring(1, msg.length), GroupCommandSource(this.subject as Group, this.sender as Member))
                else if (this.subject is User) CommandManager.execute(msg.substring(1, msg.length), UserCommandSource(this.sender))
            }
        }

        GlobalEventChannel.subscribeAlways<FriendMessageEvent> {
            sendLogger("好友消息"/*GREEN*/,sender.id, sender.nick, message)
        }

        GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
            sendLogger("群组消息"/*YELLOW*/,group.id, sender.nameCardOrNick, message)
        }

        GlobalEventChannel.subscribeAlways<GroupTempMessageEvent> {
            sendLogger("临时消息"/*CYAN*/,sender.id, sender.nameCardOrNick, message)
        }

        GlobalEventChannel.subscribeAlways<MemberJoinEvent> {
            sendLogger("群组消息"/*YELLOW*/, "用户${member.id} 加入了群组")
        }

        GlobalEventChannel.subscribeAlways<MemberLeaveEvent> {
            sendLogger("群组消息",/*YELLOW*/"用户${member.id} 离开了群组")
        }

//        GlobalEventChannel.subscribeAlways<GroupTempMessagePostSendEvent> {
//            sendLogger(
//                "发送临时消息",/*CYAN*/
//                "<${this.target.id}> [${this.target.nameCard}] ${message.contentToString()}"
//            )
//        }
//
//        GlobalEventChannel.subscribeAlways<FriendMessagePostSendEvent> {
//            sendLogger(
//                "发送好友消息",/*CYAN*/
//                "<${this.target.id}> [${this.target.nick}] ${message.contentToString()}"
//            )
//        }
//
//        GlobalEventChannel.subscribeAlways<GroupMessagePostSendEvent> {
//            sendLogger(
//                "发送群组消息",/*CYAN*/
//                "<${this.target.id}> [${bot.nick}] ${message.contentToString()}"
//            )
//        }

        GlobalEventChannel.subscribeAlways<BotOfflineEvent> {
            sendLogger("QQ状态改变", "账号 ${this.bot.id} 已离线")
        }

        GlobalEventChannel.subscribeAlways<BotReloginEvent> {
            sendLogger("QQ状态改变", "账号 ${this.bot.id} 已重新登录")
        }

    }

    private suspend fun sendLogger(title: String, id: Long, nick: String, messages: MessageChain) {
        val logger = LogManager.getLogger(title)
        val prefix = "<$id> [$nick] "
        val builder = StringBuilder()
        messages.forEach { msg ->
            builder.append(if (msg is Image) "${msg.content.substring(0, msg.content.length - 1)}(${downloadImageFile(msg)})]" else msg.content)
        }
        builder.toString().split("\n").forEach {
            logger.info(prefix + it)
        }
    }

    private fun sendLogger(prefix: String, content: String) {
        LogManager.getLogger(prefix).info(content)
    }

    private suspend fun downloadImageFile(image: Image): String {
        val date = LocalDateTime.now().toLocalDate()
        val imageDirection = File(ConsoleFiles.IMAGES.file, "${date.year}-${if (date.monthValue < 10) "0" else ""}${date.monthValue}")
        val imageFile = image.imageId.replace("""[{}]""".toRegex(), "")
        if (!imageDirection.exists()) imageDirection.mkdirs()
        Util.download(image.queryUrl(), File(imageDirection, imageFile))
        return imageFile
    }

}