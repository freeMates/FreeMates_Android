package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ItemCoursePlaceAddBinding
import com.example.freemates_android.databinding.ItemCoursePlaceBinding
import com.example.freemates_android.databinding.ItemCoursePlaceEditBinding
import com.example.freemates_android.databinding.ItemCoursePlaceFooterBinding
import com.example.freemates_android.databinding.ItemCoursePlaceHeaderBinding
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

class CourseEditAdapter(
    val context: Context,
    private val courseInfoList: List<CourseInfo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: (() -> Unit)? = null

    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
        private const val ADD_VIEW_TYPE = 2
        private const val FOOTER_VIEW_TYPE = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_VIEW_TYPE
            itemCount - 2 -> ADD_VIEW_TYPE
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
                ItemCoursePlaceEditBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseEditViewHolder(binding)
        }else if (viewType == ADD_VIEW_TYPE){
            val binding =
                ItemCoursePlaceAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            CourseInfoAddViewHolder(binding)
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
            holder.walkTime.text = ""
            holder.crosswalkCount.text = ""
        } else if(holder is CourseEditViewHolder) {
            holder.title.setText(courseInfoList[position - 1].title)
        } else if (holder is CourseInfoAddViewHolder){
            holder.itemView.setOnClickListener {
//                onCourseInfoClick()
            }
        }
    }

    override fun getItemCount(): Int = courseInfoList.size + 3
}