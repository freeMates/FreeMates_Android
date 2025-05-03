package com.example.freemates_android.ui.adapter.recommend

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemRecommendBinding

class RecommendViewHolder(binding: ItemRecommendBinding): RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivPlaceImageRecommendItem
    val title = binding.tvPlaceTitleRecommendItem
    val likeState = binding.ibtnLikeRecommendItem
    val likeCnt = binding.tvLikeCntRecommendItem
    val address = binding.tvPlaceAddressRecommendItem
    val category = binding.tvCategoryRecommendItem
    val filter = binding.rvFilterRecommendItem
}