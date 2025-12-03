package com.skillshare.skilshare_mentor.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillshare.skilshare_mentor.network.RegisterRequest
import com.skillshare.skilshare_mentor.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var day by mutableStateOf("")
    var month by mutableStateOf("")
    var year by mutableStateOf("")

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var institution by mutableStateOf("UPC")
    var country by mutableStateOf("Peru")

    var nickname by mutableStateOf("")
    var gender by mutableStateOf("Masculino")

    var isLoading by mutableStateOf(false)
    var registrationError by mutableStateOf<String?>(null)
    var isRegistered by mutableStateOf(false)

    fun registerUser() {
        isLoading = true
        registrationError = null

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    nickname = nickname,
                    institution = institution,
                    country = country,
                    gender = gender
                )

                val response = RetrofitClient.api.register(request)

                if (response.isSuccessful) {
                    isRegistered = true
                    println("✅ Registro exitoso: ${response.body()}")
                } else {
                    registrationError = "Error ${response.code()}: Credenciales inválidas o email ya existe"
                }
            } catch (e: Exception) {
                registrationError = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}