package nin.nishiki.util

import net.minecraft.client.GuiMessageTag
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MessageSignature

val sign = MessageSignature(ByteArray(256) { (it / 114).toByte() })
val tag = GuiMessageTag(-0xeebaec, null, null, "a")
val cc = Minecraft.getInstance().gui.chat

fun replaceChat(component: Component) {
    cc.deleteMessage(sign)
    cc.addMessage(component, sign, tag)
}