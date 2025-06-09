package com.example.freemates_android.ui.adapter.search

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemSearchBinding

class SearchViewHolder(binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
    val searchTitle = binding.tvSearchTitleSearch
    val textDelete = binding.btnSearchCancelSearch
}