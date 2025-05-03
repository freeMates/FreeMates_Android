package com.example.freemates_android.ui.adapter.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.databinding.ItemUserFavoriteBinding
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.map.FavoriteList

class UserFavoriteAdapter (val context: Context, val favoriteListItems: ArrayList<FavoriteList>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: FavoriteList)
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
            ItemUserFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UserFavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteListItems.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is UserFavoriteViewHolder) {
            Glide.with(context)
                .load(favoriteListItems[position].markerColor) // 불러올 이미지 url
                .into(holder.marker) // 이미지를 넣을 뷰
            holder.title.text = favoriteListItems[position].title
            Glide.with(context)
                .load(favoriteListItems[position].thumbnailUrl) // 불러올 이미지 url
                .into(holder.image) // 이미지를 넣을 뷰
            holder.favoriteCnt.text = "${favoriteListItems[position].placeCnt} 장소"
            holder.description.text = favoriteListItems[position].description
        }
    }
}