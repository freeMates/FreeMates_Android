package com.example.freemates_android.ui.adapter.favorite

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemUserFavoriteBinding
import com.example.freemates_android.model.map.FavoriteList

class UserFavoriteAdapter (val context: Context, val favoriteListItems: ArrayList<FavoriteList>, private val onItemClick: (FavoriteList) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    interface OnItemClickListener {
//        fun onItemClick(item: FavoriteList)
//    }
//
//    private var itemClickListener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: UserFavoriteAdapter.OnItemClickListener?) {
//        this.itemClickListener = listener
//    }

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
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(holder.marker) // 이미지를 넣을 뷰
            holder.title.text = favoriteListItems[position].title

            Log.d("UserFavoriteAdapter", "imageUrl : $favoriteListItems[position].thumbnailUrl")
            Glide.with(context)
                .load(favoriteListItems[position].thumbnailUrl) // 불러올 이미지 url
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(holder.image) // 이미지를 넣을 뷰

            if(favoriteListItems[position].visibilityStatus) {
                holder.visibilityLock.visibility = View.GONE
                holder.visibilityText.text = "공개"
            }
            else {
                holder.visibilityLock.visibility = View.VISIBLE
                holder.visibilityText.text = "비공개"
            }

            holder.favoriteCnt.text = "${favoriteListItems[position].places.size}개"
            holder.description.text = favoriteListItems[position].description

            holder.itemView.setOnClickListener{
                onItemClick(favoriteListItems[position])
            }
        }
    }
}