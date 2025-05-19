package com.example.freemates_android.model

data class Course(
    val courseImage: Int,
    val courseTitle: String,
    val like: Boolean,
    val likeCnt: Int,
    val courseDuration: String,
    val courseDescription: String,
    val categories: List<CategoryItem>,
)