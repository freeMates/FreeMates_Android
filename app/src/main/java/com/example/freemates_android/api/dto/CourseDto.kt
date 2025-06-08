package com.example.freemates_android.api.dto

import com.example.freemates_android.model.map.Place

data class CourseRequest(
    val title: String,
    val description: String,
    val freeTime : Int,          // ex) 30, 60 …
    val visibility: String,     // "PUBLIC" / "PRIVATE"
    val placeIds: List<String>
)

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
    val placeDtos: List<PlaceDto>,
    val likeCount: Int,
)