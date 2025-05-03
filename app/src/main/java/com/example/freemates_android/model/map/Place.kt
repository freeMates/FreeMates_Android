package com.example.freemates_android.model.map

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id: String,            // 장소 고유 ID
    val thumbnailUrl: String?,  // 썸네일 이미지 URL (null 가능)
    val name: String,           // 장소명
    val isFavorite: Boolean = false, // 현재 사용자가 찜 했는지 여부
    val categories: List<String>,    // 카테고리 목록 (예 : ["카페", "놀거리"])
    val tags: List<String>,     // 태그 목록 (예: ["조용한", "콘센트 많음"])
    val distance: String? = null,  // 현재 위치로부터 걸리는 시간
    val address: String,        // 주소
    val latitude: Double,       // 위도
    val longitude: Double,      // 경도
) : Parcelable