package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val categoryImage: Int,
    val categoryTitle: String,    // 카테고리 목록 (예 : "카페")
) : Parcelable