package com.example.freemates_android.model

data class CourseInfo(
    val title: String,
    val walkTime: Int,
    val crosswalkCount: Int,
    val categories: List<CategoryItem>,    // 카테고리 목록 (예 : ["카페", "놀거리"])
    val tags: List<FilterItem>,     // 태그 목록 (예: ["조용한", "콘센트 많음"])
    val image: Int,
)
