package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.databinding.ItemCoursePlaceAddBinding
import com.example.freemates_android.databinding.ItemCoursePlaceEditBinding
import com.example.freemates_android.databinding.ItemCoursePlaceFooterBinding
import com.example.freemates_android.databinding.ItemCoursePlaceHeaderBinding
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.model.RecommendItem

class CourseEditAdapter(
    val context: Context,
    private val courseInfoList: MutableList<RecommendItem>,
    private val onAddCourseInfoClick: (String) -> Unit
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
        } else if(holder is CourseEditViewHolder) {
            holder.title.setText(courseInfoList[position - 1].placeTitle)
            holder.title.setOnClickListener {
                Log.d("CourseEditAdapter", "itemview clicked")
                onAddCourseInfoClick("Place")
            }
        } else if (holder is CourseInfoAddViewHolder){
            holder.itemView.setOnClickListener {
                onAddCourseInfoClick("Add")
            }
        }
    }

    fun addItem(item: RecommendItem) {
        val insertPosition = courseInfoList.size + 1 // ADD_VIEW_TYPE 앞
        courseInfoList.add(item)
        notifyItemInserted(insertPosition)
    }

    override fun getItemCount(): Int = courseInfoList.size + 3
}