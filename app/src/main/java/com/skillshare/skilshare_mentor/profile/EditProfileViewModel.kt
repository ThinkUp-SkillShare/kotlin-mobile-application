package com.skillshare.skilshare_mentor.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillshare.skilshare_mentor.network.RetrofitClient
import com.skillshare.skilshare_mentor.network.UpdateUserRequest
import com.skillshare.skilshare_mentor.profile.entity.Teacher
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var saveError by mutableStateOf<String?>(null)
    var isSaved by mutableStateOf(false)

    fun saveProfile(userId: Int, teacher: Teacher) {
        isLoading = true
        saveError = null

        viewModelScope.launch {
            try {
                val request = UpdateUserRequest(
                    firstName = teacher.firstName,
                    lastName = teacher.lastName,
                    nickname = teacher.nickname,
                    institution = teacher.educationalCenter,
                    country = teacher.country,
                    gender = teacher.gender
                )

                val response = RetrofitClient.api.updateUser(userId, request)

                if (response.isSuccessful) {
                    println("✅ Perfil actualizado en DB")
                    isSaved = true
                } else {
                    saveError = "Error al guardar: ${response.code()}"
                }
            } catch (e: Exception) {
                saveError = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}