package com.example.freemates_android.api.dto

import com.example.freemates_android.model.map.Place

data class CourseResponse(
    val content: List<CourseDto>,
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

data class CourseDto(
    val courseId: String,
    val nickname: String,
    val title: String,
    val description: String,
    val freeTime: Int,
    val visibility: String,
    val imageUrl: String,
    val placeIds: List<String>,
    val coursePlaceDtos: List<PlaceDto>,
)