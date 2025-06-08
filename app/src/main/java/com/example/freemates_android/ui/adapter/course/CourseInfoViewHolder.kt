package com.example.freemates_android.ui.adapter.course

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCoursePlaceBinding

class CourseInfoViewHolder(binding: ItemCoursePlaceBinding): RecyclerView.ViewHolder(binding.root) {
    val title = binding.tvPlaceTitleCoursePlaceItem
    val walkTime = binding.tvWalkTimeCoursePlaceItem
    val category = binding.tvPlaceCategoryCoursePlaceItem
    val filter = binding.rvPlaceFilterCoursePlaceItem
    val image = binding.ivPlaceImageCoursePlaceItem
}