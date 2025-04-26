package com.example.freemates_android.ui.adapter.favorite

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemFilterBinding

class FavoriteViewHolder (binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivFavoriteImageFavoriteItem
    val title = binding.tvFavoriteTitleFavoriteItem
    val userName = binding.tvFavoriteUserNameFavoriteItem
}