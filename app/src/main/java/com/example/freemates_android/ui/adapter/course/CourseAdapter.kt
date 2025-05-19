package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCourseBinding
import com.example.freemates_android.databinding.ItemRecommendBinding
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendViewHolder
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

class CourseAdapter (val context: Context, val courseItemList: ArrayList<Course>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Course)
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
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CourseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if(courseItemList.size < 3)
            return courseItemList.size
        return 3
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CourseViewHolder) {
            Glide.with(context)
                .load(courseItemList[position].courseImage)
                .into(holder.image)
            holder.title.text = courseItemList[position].courseTitle
            holder.likeState.isSelected = courseItemList[position].like
            holder.likeCnt.text = courseItemList[position].likeCnt.toString()
            holder.duration.text = courseItemList[position].courseDuration
            holder.description.text = courseItemList[position].courseDescription

            val categoryHorizontalSpacingDecoration = HorizontalSpacingDecoration(
                context = context, // or `this` in Activity
                spacingDp = 4,              // 아이템 간 간격
            )
            val categoryAdapter = CategorySmallAdapter(context, courseItemList[position].categories){}
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