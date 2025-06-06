package com.example.freemates_android.api.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val age: Int,
    val gender: String,
    val email: String,
    val nickname: String,
)

data class RegisterResponse(
    val memberId: String,
    val username: String,
    val nickname: String,
    val mail: String
)

data class MyPageResponse(
    val username: String,
    val nickname: String,
    val email: String,
    val age: String,
    val gender: String
)