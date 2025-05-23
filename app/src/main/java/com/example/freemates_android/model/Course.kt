package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val courseImage: Int,
    val courseTitle: String,
    val courseCreator: String,
    val like: Boolean,
    val likeCnt: Int,
    val courseDuration: String,
    val courseDescription: String,
    val categories: List<CategoryItem>,
    val visibilityStatus: Boolean,
) : Parcelable