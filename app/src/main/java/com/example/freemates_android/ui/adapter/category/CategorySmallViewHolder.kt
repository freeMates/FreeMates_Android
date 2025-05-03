package com.example.freemates_android.ui.adapter.category

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCategoryBinding
import com.example.freemates_android.databinding.ItemCategorySmallBinding

class CategorySmallViewHolder (binding: ItemCategorySmallBinding, val onClick: (String) -> (Unit)): RecyclerView.ViewHolder(binding.root) {
    val container = binding.clCategoryContainerCategorySmall
    val categoryImage = binding.ivCategoryImageCategorySmall
    val categoryText = binding.tvCategoryTextCategorySmall

    init {
        binding.root.setOnClickListener {
            onClick(categoryText.text.toString())
        }
    }
}