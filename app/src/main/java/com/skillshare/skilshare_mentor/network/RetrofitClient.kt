package com.skillshare.skilshare_mentor.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @retrofit2.http.PUT("api/Users/{id}")
    suspend fun updateUser(
        @retrofit2.http.Path("id") id: Int,
        @retrofit2.http.Body request: UpdateUserRequest
    ): retrofit2.Response<Any>

    @POST("api/Group")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<Any>
}

object RetrofitClient {
    private const val BASE_URL = "https://skillshare-kotlin-backend.onrender.com/"
    //private const val BASE_URL = "http://10.0.2.2:5032/"
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}