package com.example.freemates_android.model.map

import android.os.Parcelable
import com.example.freemates_android.model.RecommendItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteList(
    val markerColor: Int,   // 마커 색상
    val title: String,  // 즐겨찾기 제목
    val thumbnailUrl: String?,  // 썸네일 이미지 URL (null 가능)
    val description: String,    // 즐겨찾기 설명
    val places: List<RecommendItem>,     // 장소 목록
    val visibilityStatus: Boolean,
    val nickname: String,
    val bookmarkId: String,
) : Parcelable
