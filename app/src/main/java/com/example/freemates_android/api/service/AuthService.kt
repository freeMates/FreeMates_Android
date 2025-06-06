package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.LoginRequest
import com.example.freemates_android.api.dto.LoginResponse
import com.example.freemates_android.api.dto.MyPageResponse
import com.example.freemates_android.api.dto.RegisterRequest
import com.example.freemates_android.api.dto.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth/login/app")
    fun loginApp(
        @Body body: LoginRequest
    ): Call<LoginResponse>

    @GET("auth/duplicate/username")
    fun duplicateUsername(
        @Query("username") username: String
    ): Call<Boolean>

    @POST("auth/register")
    fun register(
        @Body body: RegisterRequest
    ): Call<RegisterResponse>

    @GET("auth/mypage")
    fun mypage(
        @Header("Authorization") authorization: String?,
    ): Call<MyPageResponse>
}