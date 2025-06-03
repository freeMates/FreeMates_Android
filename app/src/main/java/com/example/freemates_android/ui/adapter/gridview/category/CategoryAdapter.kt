package com.example.freemates_android.ui.adapter.gridview.category

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.freemates_android.databinding.ItemCategoryBinding
import com.example.freemates_android.model.CategoryItem
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale.Category

class CategoryAdapter (val context: Context, val categoryItemList: ArrayList<CategoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CategoryViewHolder) {
            Glide.with(context)
                .load(categoryItemList[position].imageUrl) // 불러올 이미지 url
                .into(holder.categoryImage) // 이미지를 넣을 뷰
            holder.categoryText.text = categoryItemList[position].title
        }
    }
}