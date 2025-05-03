package com.example.freemates_android.ui.adapter.favorite

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemUserFavoriteBinding

class UserFavoriteViewHolder (binding: ItemUserFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
    val marker = binding.ivFavoriteMarkerUserFavoriteItem
    val title = binding.tvFavoriteTitleUserFavoriteItem
    val image = binding.ivFavoriteImageUserFavoriteItem
    val favoriteCnt = binding.tvFavoriteCntUserFavoriteItem
    val description = binding.tvFavoriteDescriptionUserFavoriteItem
}