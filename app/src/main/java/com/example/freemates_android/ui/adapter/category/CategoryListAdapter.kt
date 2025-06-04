package com.example.freemates_android.ui.adapter.category

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCategoryLargeBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem

class CategoryListAdapter (val context: Context, val items: List<Category>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCategoryLargeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategoryListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CategoryListViewHolder) {
            Glide.with(context)
                .load(items[position].categoryImage) // 불러올 이미지 url
                .into(holder.image) // 이미지를 넣을 뷰
            holder.title.text = items[position].categoryTitle
        }
    }
}