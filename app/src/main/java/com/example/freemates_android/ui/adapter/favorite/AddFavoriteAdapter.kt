package com.example.freemates_android.ui.adapter.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.model.map.AddFavorite
import com.example.freemates_android.model.map.Place

class AddFavoriteAdapter(
    val context: Context,
    private val favoriteList: List<AddFavorite>,
    private val onAddFavoriteClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: (((AddFavorite)) -> Unit)? = null

    fun setOnItemClickListener(listener: ((AddFavorite)) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_favorite_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_favorite, parent, false)
            FavoriteViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.itemView.setOnClickListener {
                onAddFavoriteClick()
            }
        } else if (holder is FavoriteViewHolder) {
            val favorite = favoriteList[position - 1] // Header 때문에 -1
            holder.tvFavoriteTitle.text = favorite.title
            holder.tvPlaceCnt.text = "${favorite.places?.size}개"
            if(favorite.visibilityStatus)
                holder.tvFavoriteVisibilityStatus.text = "공개"
            else holder.tvFavoriteVisibilityStatus.text = "비공개"

            Glide.with(context)
                .load(favorite.markerColor) // 불러올 이미지 url
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(holder.ivFavoriteMarker) // 이미지를 넣을 뷰

            Glide.with(context)
                .load(favorite.thumbnailUrl) // 불러올 이미지 url
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(holder.ivFavoriteImage) // 이미지를 넣을 뷰

            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(favorite)
            }
        }
    }

    override fun getItemCount(): Int = favoriteList.size + 1

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFavoriteMarker = view.findViewById<ImageView>(R.id.ivFavoriteMarker_AddFavoriteItem)
        val tvFavoriteTitle = view.findViewById<TextView>(R.id.tvFavoriteTitle_AddFavoriteItem)
        val tvPlaceCnt = view.findViewById<TextView>(R.id.tvPlaceCnt_AddFavoriteItem)
        val tvFavoriteVisibilityStatus = view.findViewById<TextView>(R.id.tvPlaceVisibilityStatus_AddFavoriteItem)
        val ivFavoriteImage = view.findViewById<ImageView>(R.id.ivFavoriteImage_AddFavoriteItem)
    }
}