// app/src/main/java/com/skillshare/skilshare_mentor/chat/entity/ChatMessage.kt
package com.skillshare.skilshare_mentor.chat.entity

import androidx.compose.ui.graphics.vector.ImageVector
import java.util.*

sealed class MessageType {
    object TEXT : MessageType()
    object IMAGE : MessageType()
    object FILE : MessageType()
    object SYSTEM : MessageType()
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String,
    val senderName: String,
    val senderAvatar: String? = null,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val fileName: String? = null,
    val fileSize: String? = null,
    val isSent: Boolean = true,
    val isRead: Boolean = false
)

data class ChatUser(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val isOnline: Boolean = false
)

enum class FileType(
    val extensions: List<String>,
    val icon: String
) {
    PDF(listOf("pdf"), "📄"),
    DOCUMENT(listOf("doc", "docx"), "📝"),
    SPREADSHEET(listOf("xls", "xlsx", "csv"), "📊"),
    PRESENTATION(listOf("ppt", "pptx"), "📑"),
    IMAGE(listOf("jpg", "jpeg", "png", "gif", "bmp", "webp"), "🖼️"),
    VIDEO(listOf("mp4", "avi", "mov", "mkv"), "🎥"),
    AUDIO(listOf("mp3", "wav", "ogg"), "🎵"),
    OTHER(listOf(), "📎")
}