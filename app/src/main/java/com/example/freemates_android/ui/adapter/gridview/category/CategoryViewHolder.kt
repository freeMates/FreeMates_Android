package com.example.freemates_android.ui.adapter.gridview.category

import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import com.example.freemates_android.databinding.ItemCategoryBinding
import com.example.freemates_android.model.CategoryItem

class CategoryViewHolder(binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
    val categoryImage = binding.ivCategoryImageCategory
    val categoryText = binding.tvCategoryTextCategory
}