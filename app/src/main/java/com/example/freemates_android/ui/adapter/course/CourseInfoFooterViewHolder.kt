package com.example.freemates_android.ui.adapter.course

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCoursePlaceFooterBinding
import com.example.freemates_android.databinding.ItemCoursePlaceHeaderBinding

class CourseInfoFooterViewHolder(binding: ItemCoursePlaceFooterBinding): RecyclerView.ViewHolder(binding.root) {
    val walkTime = binding.tvWalkTimeCoursePlaceItemFooter
    val crosswalkCount = binding.tvCrosswalkCountCoursePlaceItemFooter
}