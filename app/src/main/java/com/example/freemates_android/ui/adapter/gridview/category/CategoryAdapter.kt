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
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.FavoriteItem

class CategoryAdapter (val context: Context, val categoryItemList: ArrayList<Category>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Category)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

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
                .load(categoryItemList[position].categoryImage) // 불러올 이미지 url
                .into(holder.categoryImage) // 이미지를 넣을 뷰
            holder.categoryText.text = categoryItemList[position].categoryTitle

            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(categoryItemList[position])
            }
        }
    }
}