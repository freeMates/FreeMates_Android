package com.example.freemates_android.model.map

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteList(
    val markerColor: Int,   // 마커 색상
    val title: String,  // 즐겨찾기 제목
    val thumbnailUrl: Int,  // 썸네일 이미지 URL (null 가능)
    val placeCnt: Int,  // 즐겨찾기 속 장소 개수
    val description: String,    // 즐겨찾기 설명
) : Parcelable
