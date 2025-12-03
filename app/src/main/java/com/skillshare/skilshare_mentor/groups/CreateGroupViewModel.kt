package com.skillshare.skilshare_mentor.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillshare.skilshare_mentor.network.CreateGroupRequest
import com.skillshare.skilshare_mentor.network.RetrofitClient
import kotlinx.coroutines.launch

class CreateGroupViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var createError by mutableStateOf<String?>(null)
    var isCreated by mutableStateOf(false)

    fun createGroup(
        name: String,
        subject: String,
        topic: String,
        description: String,
        privacy: String,
        creatorId: Int
    ) {
        isLoading = true
        createError = null

        viewModelScope.launch {
            try {
                val request = CreateGroupRequest(
                    name = name,
                    subject = subject,
                    topic = topic,
                    description = description,
                    privacy = privacy,
                    creatorUserId = creatorId
                )

                val response = RetrofitClient.api.createGroup(request)

                if (response.isSuccessful) {
                    isCreated = true
                } else {
                    createError = "Error ${response.code()}: No se pudo crear el grupo"
                }
            } catch (e: Exception) {
                createError = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        isCreated = false
        createError = null
        isLoading = false
    }
}