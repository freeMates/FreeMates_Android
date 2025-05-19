package com.example.freemates_android.ui.adapter.course

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCourseBinding
import com.example.freemates_android.databinding.ItemRecommendBinding

class CourseViewHolder(binding: ItemCourseBinding): RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivCourseImageCourseItem
    val title = binding.tvCourseTitleCourseItem
    val likeState = binding.ivCourseImageCourseItem
    val likeCnt = binding.tvLikeCntCourseItem
    val duration = binding.tvCourseDurationCourseItem
    val description = binding.tvCourseDescriptionCourseItem
    val category = binding.rvCourseCategoryCourseItem
}