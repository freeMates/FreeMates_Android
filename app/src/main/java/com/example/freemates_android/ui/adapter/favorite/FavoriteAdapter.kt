package com.example.freemates_android.ui.adapter.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.RecommendItem

class FavoriteAdapter(val context: Context, val favoriteItemList: ArrayList<FavoriteItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: FavoriteItem)
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
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is FavoriteViewHolder) {
            Glide.with(context)
                .load(favoriteItemList[position].imageUrl) // 불러올 이미지 url
                .into(holder.image) // 이미지를 넣을 뷰
            holder.title.text = favoriteItemList[position].title
            holder.userName.text = "@${favoriteItemList[position].userName}"

            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(favoriteItemList[position])
            }
        }
    }
}