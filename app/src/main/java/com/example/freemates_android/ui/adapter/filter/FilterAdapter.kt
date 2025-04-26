package com.example.freemates_android.ui.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemFilterBinding
import com.example.freemates_android.model.FilterItem

class FilterAdapter(val context: Context, val filterItemList: List<FilterItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is FilterViewHolder) {
            holder.filterText.text = filterItemList[position].filterText
        }
    }
}