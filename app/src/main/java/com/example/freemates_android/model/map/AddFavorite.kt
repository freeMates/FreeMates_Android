package com.example.freemates_android.model.map

import com.example.freemates_android.model.RecommendItem

data class AddFavorite (
    val markerColor: Int,   // 마커 색상
    val title: String,  // 즐겨찾기 제목
    val thumbnailUrl: Int,  // 썸네일 이미지 URL (null 가능)
    val places: List<RecommendItem>,     // 장소 목록
    val visibilityStatus: Boolean       // 공개 여부
)