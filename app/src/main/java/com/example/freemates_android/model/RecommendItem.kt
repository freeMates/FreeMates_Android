package com.example.freemates_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendItem(
    val placeImage: Int,
    val placeTitle: String,
    val like: Boolean,
    val likeCnt: Int,
    val placeAddress: String,
    val placeCategoryImage: Int,
    val placeCategoryTitle: String,
    val filter: List<String>
): Parcelable