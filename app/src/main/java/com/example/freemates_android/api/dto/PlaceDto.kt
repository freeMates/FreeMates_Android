package com.example.freemates_android.api.dto

data class CategoryResponse(
    val content: List<PlaceDto>,
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

data class PlaceDto(
    val placeId: String,
    val placeName: String,
    val introText: String,
    val addressName: String,
    val imageUrl: String,
    val tags: List<String>,
    val categoryType: String,
    val likeCount: Int,
    val viewCount: Int,
    val distance: String,
    val y: String,
    val x: String,
)

data class PageableDto(
    val pageNumber: Int,
    val pageSize: Int,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean,
    val sort: SortDto
)

data class SortDto(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class GeocodeDto(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)