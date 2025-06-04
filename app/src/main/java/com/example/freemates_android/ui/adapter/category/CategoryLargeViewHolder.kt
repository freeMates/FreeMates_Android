package com.example.freemates_android.ui.adapter.category

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCategoryLargeBinding
import com.example.freemates_android.model.CategoryItem

class CategoryLargeViewHolder(
    private val binding: ItemCategoryLargeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryItem, onClick: (CategoryItem) -> Unit) = with(binding) {
        tvCategoryTextCategoryLarge.text  = item.title
        ivCategoryImageCategoryLarge.setImageResource(item.currentIcon)

        // 배경·글자색도 isSelected 에 따라 처리
        val bgColor   = if (item.isSelected) R.color.primary400 else R.color.natural100
        val textColor = if (item.isSelected) R.color.secondary200 else R.color.natural200
        ViewCompat.setBackgroundTintList(
            clCategoryContainerCategoryLarge,
            ColorStateList.valueOf(ContextCompat.getColor(root.context, bgColor))
        )
        tvCategoryTextCategoryLarge.setTextColor(
            ContextCompat.getColor(root.context, textColor)
        )

        root.setOnClickListener { onClick(item) }
    }
}