package com.example.freemates_android.ui.adapter.recommend

import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemRecommendBinding

class RecommendViewHolder(binding: ItemRecommendBinding): RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivPlaceImageRecommend
    val title = binding.tvPlaceTitleRecommend
    val likeState = binding.ibtnLikeRecommend
    val likeCnt = binding.tvLikeCntRecommend
    val address = binding.tvPlaceAddressRecommend
    val category = binding.tvCategoryHome
    val filter = binding.rvFilterHome
}