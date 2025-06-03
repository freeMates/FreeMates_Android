package com.example.freemates_android.api.dto

data class ApiResponse<T>(
    val code: Int,        // 또는 status
    val message: String,
    val data: T?
)