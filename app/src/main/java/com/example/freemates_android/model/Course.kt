package com.example.freemates_android.model

import android.os.Parcelable
import com.example.freemates_android.model.map.Place
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val courseImage: String?,
    val courseTitle: String,
    val courseCreator: String?,
    var like: Boolean,
    val likeCnt: Int,
    val courseDuration: String,
    val courseDescription: String,
    var places: List<RecommendItem>,
    val categories: List<Category>,
    val visibilityStatus: Boolean,
) : Parcelable