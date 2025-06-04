package com.example.freemates_android.ui.adapter.category

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCategoryLargeBinding
import com.example.freemates_android.model.CategoryItem

class CategoryListViewHolder(binding: ItemCategoryLargeBinding) : RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivCategoryImageCategoryLarge
    val title = binding.tvCategoryTextCategoryLarge
}