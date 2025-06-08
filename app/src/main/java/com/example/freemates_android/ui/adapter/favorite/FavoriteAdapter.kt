package com.example.freemates_android.ui.adapter.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemFavoriteBinding
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.map.FavoriteList

class FavoriteAdapter(val context: Context, private val favoriteItemList: ArrayList<FavoriteList>) :
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is FavoriteViewHolder) {
//            val imageUrl = favoriteItemList[position].thumbnailUrl?.replaceFirst("http://", "https://")
            Glide.with(context)
                .load(favoriteItemList[position].thumbnailUrl) // 불러올 이미지 url
                .placeholder(R.drawable.ic_image_default) // 로딩 중
                .error(R.drawable.ic_image_default)       // 404 등 에러
                .fallback(R.drawable.ic_image_default)
                .into(holder.image) // 이미지를 넣을 뷰
            holder.title.text = favoriteItemList[position].title
            holder.userName.text = "@${favoriteItemList[position].nickname}"

            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(favoriteItemList[position])
            }
        }
    }
}