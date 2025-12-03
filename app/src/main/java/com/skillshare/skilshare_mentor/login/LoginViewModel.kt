package com.skillshare.skilshare_mentor.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillshare.skilshare_mentor.network.LoginRequest
import com.skillshare.skilshare_mentor.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)
    var isLoginSuccessful by mutableStateOf(false)

    var loggedInUserName by mutableStateOf("")
    var loggedInLastName by mutableStateOf("")
    var loggedInInstitution by mutableStateOf("")
    var loggedInUserId by mutableStateOf<Int?>(null)

    fun loginUser() {
        isLoading = true
        loginError = null

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        loggedInUserName = data.firstName
                        loggedInLastName = data.lastName
                        loggedInInstitution = data.institution
                        loggedInUserId = data.userId

                        isLoginSuccessful = true
                    }
                } else {
                    loginError = "Error: Credenciales incorrectas"
                }
            } catch (e: Exception) {
                loginError = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}