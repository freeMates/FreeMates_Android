package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCoursePlaceBinding
import com.example.freemates_android.databinding.ItemCoursePlaceFooterBinding
import com.example.freemates_android.databinding.ItemCoursePlaceHeaderBinding
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.adapter.category.CategoryListAdapter
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

class CourseInfoAdapter(
    val context: Context,
    private val courseInfoList: List<CourseInfo>
//    private val onCourseInfoClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: (() -> Unit)? = null
    private var walkTime: Int = 0
    private var crosswalkCount: Int = 0

    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_VIEW_TYPE
            itemCount - 1 -> FOOTER_VIEW_TYPE
            else -> ITEM_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_VIEW_TYPE) {
            val binding =
                ItemCoursePlaceHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseInfoHeaderViewHolder(binding)
        } else if (viewType == ITEM_VIEW_TYPE){
            val binding =
                ItemCoursePlaceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseInfoViewHolder(binding)
        } else {
            Log.d("아이템 뷰 : ", "footer")
            val binding =
                ItemCoursePlaceFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseInfoFooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CourseInfoHeaderViewHolder) {
            holder.itemView.setOnClickListener {
//                onCourseInfoClick()
            }
        } else if (holder is CourseInfoFooterViewHolder) {
            Log.d("아이템 뷰 : ", "footer")
            if(walkTime > 0)
                holder.walkTime.text = "도보 30분"
            else
                holder.walkTime.text = ""

            if(crosswalkCount > 0)
                holder.crosswalkCount.text = "횡단보도 5회"
            else
                holder.crosswalkCount.text = ""
        } else if (holder is CourseInfoViewHolder){
            val pos = position - 1
            holder.title.text = courseInfoList[pos].title

            walkTime += courseInfoList[pos].walkTime
            crosswalkCount += courseInfoList[pos].crosswalkCount
            if(courseInfoList[pos].walkTime > 0)
                holder.walkTime.text = "도보 ${courseInfoList[pos].walkTime}분"
            else
                holder.walkTime.text = ""

            if(courseInfoList[pos].crosswalkCount > 0)
                holder.crosswalkCount.text = "횡단보도 ${courseInfoList[pos].crosswalkCount}회"
            else
                holder.crosswalkCount.text = ""

            Glide.with(context)
                .load(courseInfoList[pos].image)
                .into(holder.image)

            val horizontalSpacingDecoration = HorizontalSpacingDecoration(
                context = context, // or `this` in Activity
                spacingDp = 4,              // 아이템 간 간격
            )
            val categoryAdapter = CategoryListAdapter(context, courseInfoList[pos].categories)
            holder.category.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.category.addItemDecoration(horizontalSpacingDecoration)
            holder.category.adapter = categoryAdapter

            val filterAdapter = FilterAdapter(context, courseInfoList[pos].tags)
            holder.filter.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.filter.addItemDecoration(horizontalSpacingDecoration)
            holder.filter.adapter = filterAdapter
        }
    }

    override fun getItemCount(): Int = courseInfoList.size + 2
}