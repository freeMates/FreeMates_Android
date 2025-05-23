package com.example.freemates_android.ui.adapter.course

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCoursePlaceBinding

class CourseInfoViewHolder(binding: ItemCoursePlaceBinding): RecyclerView.ViewHolder(binding.root) {
    val title = binding.tvPlaceTitleCoursePlaceItem
    val walkTime = binding.tvWalkTimeCoursePlaceItem
    val crosswalkCount = binding.tvCrosswalkCountCoursePlaceItem
    val category = binding.rvPlaceCategoryCoursePlaceItem
    val filter = binding.rvPlaceFilterCoursePlaceItem
    val image = binding.ivPlaceImageCoursePlaceItem
}