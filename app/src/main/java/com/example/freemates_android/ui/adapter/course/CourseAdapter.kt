package com.example.freemates_android.ui.adapter.course

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ItemCourseBinding
import com.example.freemates_android.databinding.ItemCourseHeaderBinding
import com.example.freemates_android.model.Course
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.adapter.category.CategoryListAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration

sealed class DisplayItem {
    data class Header(val title: String) : DisplayItem()
    data class CourseRow(val course: Course) : DisplayItem()
}

class CourseAdapter(
    private val context: Context,
    private val courseItemList: List<Course>,
    private val courseHeaderItemList: List<String>,   // 0:좋아요, 1~:30분~3시간
    private val state: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE   = 1
    }

    private val displayList = mutableListOf<DisplayItem>()

    init { buildDisplayList() }

    private fun buildDisplayList() {
        displayList.clear()

        if (state == "recommend") {
            // 추천: 그냥 앞에서 3개만 보여줌 (헤더 없이)
            courseItemList.take(3).forEach {
                displayList += DisplayItem.CourseRow(it)
            }
        } else if (state == "durationCourse") {
            // ➊ 좋아요한 코스
            val likedCourses = courseItemList.filter { it.like }
            if (likedCourses.isNotEmpty()) {
                displayList += DisplayItem.Header(courseHeaderItemList.getOrNull(0) ?: "추천 코스")
                likedCourses.forEach { displayList += DisplayItem.CourseRow(it) }
            }

            // ➋ Duration 순서대로 그룹핑
            val durationOrder = listOf(
                "30분 소요 코스", "1시간 소요 코스", "1시간 30분 소요 코스",
                "2시간 소요 코스", "2시간 30분 소요 코스", "3시간 소요 코스"
            )

            durationOrder.forEachIndexed { idx, duration ->
                val group = courseItemList.filter { it.courseDuration == duration && !it.like }
                if (group.isNotEmpty()) {
                    val headerText = courseHeaderItemList.getOrNull(idx + 1) ?: duration
                    displayList += DisplayItem.Header(headerText)
                    group.forEach { displayList += DisplayItem.CourseRow(it) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (displayList[position]) {
            is DisplayItem.Header    -> HEADER_VIEW_TYPE
            is DisplayItem.CourseRow -> ITEM_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == HEADER_VIEW_TYPE) {
            val binding = ItemCourseHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            CourseHeaderViewHolder(binding)
        } else {
            val binding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            CourseViewHolder(binding)
        }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayList[position]) {
            is DisplayItem.Header -> {
                (holder as CourseHeaderViewHolder).duration.text = item.title
            }

            is DisplayItem.CourseRow -> {
                val course = item.course
                (holder as CourseViewHolder).apply {
                    val imageUrl = course.courseImage?.replaceFirst("http://", "https://")
                    Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image_default)
                        .error(R.drawable.ic_image_default)
                        .fallback(R.drawable.ic_image_default)
                        .into(image)

                    title.text = course.courseTitle
                    likeState.isSelected = course.like
                    likeCnt.text = course.likeCnt.toString()
                    duration.text = course.courseDuration
                    description.text = course.courseDescription

                    val spacing = HorizontalSpacingDecoration(context, 4)
                    category.apply {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        if (itemDecorationCount == 0) addItemDecoration(spacing)
                        adapter = CategoryListAdapter(context, course.categories)
                    }

                    likeState.setOnClickListener {
                        val now = likeState.isSelected
                        likeCnt.text = (if (now) course.likeCnt - 1 else course.likeCnt + 1).toString()
                        likeState.isSelected = !now
                        course.like = !now // 이 라인은 필요에 따라 유지/삭제
                    }

                    itemView.setOnClickListener {
                        itemClickListener?.onItemClick(course)
                    }
                }
            }
        }
    }

    // 클릭 리스너
    interface OnItemClickListener { fun onItemClick(item: Course) }
    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) { itemClickListener = listener }
}

//    interface OnItemClickListener {
//        fun onItemClick(item: Course)
//    }
//
//    private var itemClickListener: OnItemClickListener? = null
//    private var pos = 0
//
//    init {
//        pos = 0
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.itemClickListener = listener
//    }
//
//    companion object {
//        private const val HEADER_VIEW_TYPE = 0
//        private const val ITEM_VIEW_TYPE = 1
//        private val HEADER_POSITIONS = setOf(0, 6, 10, 14, 18, 22, 26)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (state == "recommend") ITEM_VIEW_TYPE
//        else if (HEADER_POSITIONS.contains(position)) HEADER_VIEW_TYPE
//        else ITEM_VIEW_TYPE
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): RecyclerView.ViewHolder {
//        return if (viewType == CourseAdapter.HEADER_VIEW_TYPE) {
//            val binding =
//                ItemCourseHeaderBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            CourseHeaderViewHolder(binding)
//        } else {
//            val binding =
//                ItemCourseBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            CourseViewHolder(binding)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if (state == "recommend") {
//            if (courseItemList.size < 3){
//                return courseItemList.size
//            }
//            return 3
//        }
//        else {
////            if(courseItemList)
//            return 7 + 23
//        }
//    }
//
//    override fun onBindViewHolder(
//        holder: RecyclerView.ViewHolder,
//        position: Int
//    ) {
//        if(holder is CourseHeaderViewHolder){
//            holder.duration.text = courseHeaderItemList[pos]
//            pos += 1
//            Log.d("헤더 값 : ", "11111111111111111")
////            holder.itemView.setOnClickListener {
////                onAddFavoriteClick()
////            }
//        }
//        else if (holder is CourseViewHolder) {
//            var holderPos = position
////            if (holderPos != 0 || holderPos != 6 || holderPos != 10 || holderPos != 14 || holderPos != 18 || holderPos != 22 || holderPos != 26)
//            holderPos -= pos
//            Log.d("pos :: ", pos.toString())
//            Log.d("포지션 :: ", holderPos.toString())
//            val imageUrl = courseItemList[holderPos].courseImage?.replaceFirst("http://", "https://")
//            Glide.with(context)
//                .load(imageUrl)
//                .placeholder(R.drawable.ic_image_default)
//                .error(R.drawable.ic_image_default)
//                .fallback(R.drawable.ic_image_default)
//                .into(holder.image)
//            holder.title.text = courseItemList[holderPos].courseTitle
//            holder.likeState.isSelected = courseItemList[holderPos].like
//            holder.likeCnt.text = courseItemList[holderPos].likeCnt.toString()
//            holder.duration.text = courseItemList[holderPos].courseDuration
//            holder.description.text = courseItemList[holderPos].courseDescription
//
//            val categoryHorizontalSpacingDecoration = HorizontalSpacingDecoration(
//                context = context, // or `this` in Activity
//                spacingDp = 4,              // 아이템 간 간격
//            )
//            val categoryAdapter = CategoryListAdapter(context, courseItemList[holderPos].categories)
//            holder.category.layoutManager =
//                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
//            holder.category.addItemDecoration(categoryHorizontalSpacingDecoration)
//            holder.category.adapter = categoryAdapter
//
//            holder.likeState.setOnClickListener {
//                val isState = holder.likeState.isSelected
//                val likeCnt = holder.likeCnt.text.toString().toInt()
//
//                if (isState) {
//                    holder.likeCnt.text = (likeCnt - 1).toString()
//                } else {
//                    holder.likeCnt.text = (likeCnt + 1).toString()
//                }
//                holder.likeState.isSelected = !isState
//            }
//
//            holder.itemView.setOnClickListener {
//                itemClickListener?.onItemClick(courseItemList[position])
//            }
//        }
//    }
//}