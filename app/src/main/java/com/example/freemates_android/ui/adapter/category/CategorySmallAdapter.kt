package com.example.freemates_android.ui.adapter.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCategorySmallBinding
import com.example.freemates_android.databinding.ItemFilterBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.ui.adapter.filter.FilterViewHolder

class CategorySmallAdapter (val context: Context, val categorySmallItemList: List<CategoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCategorySmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategorySmallViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categorySmallItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CategorySmallViewHolder) {
            Glide.with(context)
                .load(categorySmallItemList[position].imageUrl) // 불러올 이미지 url
                .into(holder.categoryImage) // 이미지를 넣을 뷰
            holder.categoryText.text = categorySmallItemList[position].title
        }
    }
}