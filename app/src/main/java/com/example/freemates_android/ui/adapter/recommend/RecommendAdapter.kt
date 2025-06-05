package com.example.freemates_android.ui.adapter.recommend

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemRecommendBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

class RecommendAdapter (val context: Context, val recommendItemList: ArrayList<RecommendItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: RecommendItem)
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
            ItemRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RecommendViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recommendItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is RecommendViewHolder) {
            val imageUrl = recommendItemList[position].placeImage?.replaceFirst("http://", "https://")
            Glide.with(context)
                .load(imageUrl)
                .into(holder.image)
            holder.title.text = recommendItemList[position].placeTitle
            holder.likeState.isSelected = recommendItemList[position].like == true
            holder.likeCnt.text = recommendItemList[position].likeCnt.toString()
            holder.address.text = recommendItemList[position].placeAddress
            holder.category.text = recommendItemList[position].placeCategoryTitle
            val drawable = recommendItemList[position].placeCategoryImage?.let {
                ContextCompat.getDrawable(context,
                    it
                )
            }
            holder.category.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            val filterHorizontalSpacingDecoration = HorizontalSpacingDecoration(
                context = context, // or `this` in Activity
                spacingDp = 2,              // 아이템 간 간격
            )
            val filterAdapter = recommendItemList[position].filter?.let {
                FilterAdapter(context,
                    it
                )
            }
            holder.filter.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.filter.addItemDecoration(filterHorizontalSpacingDecoration)
            holder.filter.adapter = filterAdapter

            holder.filter.setOnTouchListener { _, _ -> true }

            holder.likeState.setOnClickListener{
                val isState = holder.likeState.isSelected
                val likeCnt = holder.likeCnt.text.toString().toInt()

                if(isState){
                    holder.likeCnt.text = (likeCnt - 1).toString()
                } else{
                    holder.likeCnt.text = (likeCnt + 1).toString()
                }
                holder.likeState.isSelected = !isState
            }

            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(recommendItemList[position])
            }
        }
    }
}