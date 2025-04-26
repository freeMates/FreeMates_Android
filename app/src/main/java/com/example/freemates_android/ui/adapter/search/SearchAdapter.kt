package com.example.freemates_android.ui.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCategorySmallBinding
import com.example.freemates_android.databinding.ItemSearchBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.SearchItem
import com.example.freemates_android.ui.adapter.category.CategorySmallViewHolder

class SearchAdapter (val context: Context, val searchItemList: List<SearchItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is SearchViewHolder) {
            holder.searchTitle.text = searchItemList[position].searchTitle
        }
    }
}