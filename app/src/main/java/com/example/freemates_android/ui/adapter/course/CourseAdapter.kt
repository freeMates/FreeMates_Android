package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCourseBinding
import com.example.freemates_android.databinding.ItemCourseHeaderBinding
import com.example.freemates_android.model.Course
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

class CourseAdapter(val context: Context, val courseItemList: ArrayList<Course>, val courseHeaderItemList: ArrayList<String>, val state: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Course)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var pos = 0

    init {
        pos = 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
        private val HEADER_POSITIONS = setOf(0, 6, 10, 14, 18, 22, 26)
    }

    override fun getItemViewType(position: Int): Int {
        return if (state == "recommend") ITEM_VIEW_TYPE
        else if (HEADER_POSITIONS.contains(position)) HEADER_VIEW_TYPE
        else ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == CourseAdapter.HEADER_VIEW_TYPE) {
            val binding =
                ItemCourseHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseHeaderViewHolder(binding)
        } else {
            val binding =
                ItemCourseBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        if (state == "recommend") {
            if (courseItemList.size < 3){
                return courseItemList.size
            }
            return 3
        }
        else {
//            if(courseItemList)
            return 7 + 23
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if(holder is CourseHeaderViewHolder){
            holder.duration.text = courseHeaderItemList[pos]
            pos += 1
            Log.d("헤더 값 : ", "11111111111111111")
//            holder.itemView.setOnClickListener {
//                onAddFavoriteClick()
//            }
        }
        else if (holder is CourseViewHolder) {
            var holderPos = position
//            if (holderPos != 0 || holderPos != 6 || holderPos != 10 || holderPos != 14 || holderPos != 18 || holderPos != 22 || holderPos != 26)
            holderPos -= pos
            Log.d("pos :: ", pos.toString())
            Log.d("포지션 :: ", holderPos.toString())
            Glide.with(context)
                .load(courseItemList[holderPos].courseImage)
                .into(holder.image)
            holder.title.text = courseItemList[holderPos].courseTitle
            holder.likeState.isSelected = courseItemList[holderPos].like
            holder.likeCnt.text = courseItemList[holderPos].likeCnt.toString()
            holder.duration.text = courseItemList[holderPos].courseDuration
            holder.description.text = courseItemList[holderPos].courseDescription

            val categoryHorizontalSpacingDecoration = HorizontalSpacingDecoration(
                context = context, // or `this` in Activity
                spacingDp = 4,              // 아이템 간 간격
            )
            val categoryAdapter = CategorySmallAdapter(context, courseItemList[holderPos].categories){}
            holder.category.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.category.addItemDecoration(categoryHorizontalSpacingDecoration)
            holder.category.adapter = categoryAdapter

            holder.likeState.setOnClickListener {
                val isState = holder.likeState.isSelected
                val likeCnt = holder.likeCnt.text.toString().toInt()

                if (isState) {
                    holder.likeCnt.text = (likeCnt - 1).toString()
                } else {
                    holder.likeCnt.text = (likeCnt + 1).toString()
                }
                holder.likeState.isSelected = !isState
            }

            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(courseItemList[position])
            }
        }
    }
}