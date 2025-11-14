package com.skillshare.skilshare_mentor.chat

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillshare.skilshare_mentor.chat.entity.ChatMessage
import com.skillshare.skilshare_mentor.chat.entity.MessageType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    var currentMessage by mutableStateOf("")
        private set

    var selectedFiles by mutableStateOf<List<Uri>>(emptyList())
        private set

    var showAttachmentOptions by mutableStateOf(false)
        private set

    private val currentUser = "user_123"

    init {
        loadSampleMessages()
    }

    fun onMessageChange(message: String) {
        currentMessage = message
    }

    fun sendMessage() {
        if (currentMessage.isNotBlank() || selectedFiles.isNotEmpty()) {
            viewModelScope.launch {
                if (currentMessage.isNotBlank()) {
                    val textMessage = ChatMessage(
                        senderId = currentUser,
                        senderName = "Tú",
                        content = currentMessage,
                        messageType = MessageType.TEXT
                    )
                    _messages.value = _messages.value + textMessage
                    currentMessage = ""

                    delay(1000)
                    val responseMessage = ChatMessage(
                        senderId = "group_ai",
                        senderName = "Physics Bot",
                        content = "¡Interesante pregunta! ¿Te gustaría que profundice en algún aspecto específico?",
                        messageType = MessageType.TEXT
                    )
                    _messages.value = _messages.value + responseMessage
                }

                selectedFiles.forEach { fileUri ->
                    val fileName = getFileNameFromUri(fileUri)
                    val fileMessage = ChatMessage(
                        senderId = currentUser,
                        senderName = "Tú",
                        content = "Archivo compartido: $fileName",
                        messageType = MessageType.FILE,
                        fileName = fileName,
                        fileSize = "2.4 MB"
                    )
                    _messages.value = _messages.value + fileMessage
                }

                selectedFiles = emptyList()
            }
        }
    }

    fun addAttachment(uri: Uri) {
        selectedFiles = selectedFiles + uri
        showAttachmentOptions = false
    }

    fun removeAttachment(uri: Uri) {
        selectedFiles = selectedFiles.filter { it != uri }
    }

    fun clearAttachments() {
        selectedFiles = emptyList()
    }

    fun toggleAttachmentOptions() {
        showAttachmentOptions = !showAttachmentOptions
    }

    fun startRecording() {
        _isRecording.value = true
    }

    fun stopRecording() {
        _isRecording.value = false
    }

    private fun loadSampleMessages() {
        _messages.value = listOf(
            ChatMessage(
                senderId = "group_ai",
                senderName = "Physics Bot",
                content = "¡Bienvenidos al grupo de estudio de Physics 101! ¿En qué puedo ayudarles hoy?",
                timestamp = System.currentTimeMillis() - 3600000
            ),
            ChatMessage(
                senderId = "user_456",
                senderName = "María González",
                content = "Hola a todos, tengo una duda sobre la ley de Newton",
                timestamp = System.currentTimeMillis() - 1800000
            )
        )
    }

    private fun getFileNameFromUri(uri: Uri): String {
        return "document_${System.currentTimeMillis()}"
    }
}