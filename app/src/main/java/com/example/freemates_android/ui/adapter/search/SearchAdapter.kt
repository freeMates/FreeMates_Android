package com.example.freemates_android.ui.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemSearchBinding
import com.example.freemates_android.model.SearchItem
import com.example.freemates_android.saveStringList

class SearchAdapter (val context: Context, val searchItemList: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is SearchViewHolder) {
            holder.searchTitle.text = searchItemList[position]

            holder.textDelete.setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    searchItemList.removeAt(pos)

                    notifyItemRemoved(pos)

                    context.saveStringList(searchItemList)
                }
            }
        }
    }
}