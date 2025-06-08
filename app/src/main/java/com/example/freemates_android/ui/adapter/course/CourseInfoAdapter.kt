package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCoursePlaceBinding
import com.example.freemates_android.databinding.ItemCoursePlaceFooterBinding
import com.example.freemates_android.databinding.ItemCoursePlaceHeaderBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.adapter.category.CategoryListAdapter
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import kotlin.math.ceil

class CourseInfoAdapter(
    val context: Context,
    private val courseInfoList: List<RecommendItem>
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

            holder.walkTime.text = "도보 ${walkTime}분"

        } else if (holder is CourseInfoViewHolder){
            val pos = position - 1
            holder.title.text = courseInfoList[pos].placeTitle

            val minutes = courseInfoList[pos].placeDuration?.let { minutesFromDistance(it.toDouble()) }
            holder.walkTime.text = "도보 ${minutes?.let { formatMinutes(it) }}"
            if (minutes != null) {
                walkTime += minutes
            }

            Glide.with(context)
                .load(courseInfoList[pos].placeImage)
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(holder.image)

            val horizontalSpacingDecoration = HorizontalSpacingDecoration(
                context = context, // or `this` in Activity
                spacingDp = 4,              // 아이템 간 간격
            )

            val drawable = courseInfoList[pos].placeCategoryImage?.let {
                ContextCompat.getDrawable(context,
                    it
                )
            }
            holder.category.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

            val filterAdapter = courseInfoList[pos].filter?.let { FilterAdapter(context, it) }
            holder.filter.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.filter.addItemDecoration(horizontalSpacingDecoration)
            holder.filter.adapter = filterAdapter
        }
    }

    private fun minutesFromDistance(distanceMeters: Double,
                                    speedMps: Double = 1.4): Int {
        val seconds = distanceMeters / speedMps
        return ceil(seconds / 60).toInt()
    }

    private fun formatMinutes(koreanMinutes: Int): String {
        if (koreanMinutes < 1) return "1분 미만"
        val hours = koreanMinutes / 60
        val minutes = koreanMinutes % 60
        return when {
            hours == 0        -> "${minutes}분"
            minutes == 0      -> "${hours}시간"
            else              -> "${hours}시간 ${minutes}분"
        }
    }

    override fun getItemCount(): Int = courseInfoList.size + 2
}