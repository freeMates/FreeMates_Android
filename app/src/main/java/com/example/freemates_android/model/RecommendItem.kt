package com.example.freemates_android.model

data class RecommendItem(
    val placeImage: Int,
    val placeTitle: String,
    val like: Boolean,
    val likeCnt: Int,
    val placeAddress: String,
    val placeCategoryImage: Int,
    val placeCategoryTitle: String,
    val filter: List<FilterItem>
)