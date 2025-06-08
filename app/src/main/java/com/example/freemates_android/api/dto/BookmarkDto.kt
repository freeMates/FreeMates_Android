package com.example.freemates_android.api.dto

data class BookmarkCreateResponse(
    val memberId: String,
    val nickname: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val pinColor: String,
    val visibility: String,
)

data class MyBookmarkListResponse(
    val bookmarkId: String?,
    val imageUrl: String,       // "/uploads/xxxx.jpg" 형태
    val title: String,
    val description: String,
    val pinColor: String,       // "RED", "BLUE", ... 등의 문자열
    val visibility: String,     // "PUBLIC" 또는 "PRIVATE"
    val memberId: String,
    val nickname: String,
    val likeCount : Int,
    val placeDtos: List<PlaceDto>?
)

data class PageBookmarkResponse(
    val content: List<MyBookmarkListResponse>,
    val pageable: PageableDto,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val first: Boolean,
    val size: Int,
    val number: Int,
    val sort: SortDto,
    val numberOfElements: Int,
    val empty: Boolean
)