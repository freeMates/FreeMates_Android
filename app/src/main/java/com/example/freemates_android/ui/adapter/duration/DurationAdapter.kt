package com.example.freemates_android.ui.adapter.duration

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemDurationFilterBinding

class DurationAdapter(val context: Context, val durationItemList: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: String)
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
            ItemDurationFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DurationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return durationItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is DurationViewHolder) {
            holder.duration.text = durationItemList[position]

//            holder.filter.setOnTouchListener { _, _ -> true }

//            if(position == 2)
//                holder.itemView.isSelected = true

//            holder.itemView.setOnClickListener {
//                itemClickListener?.onItemClick(durationItemList[position])
//            }
        }
    }
}