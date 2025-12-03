package com.skillshare.skilshare_mentor.network

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val institution: String,
    val country: String,
    val gender: String
)

data class RegisterResponse(
    val message: String?,
    val userId: Int?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val institution: String
)

data class UpdateUserRequest(
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val institution: String,
    val country: String,
    val gender: String
)