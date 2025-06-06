package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentDurationCourseBinding
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.course.CourseAdapter
import com.example.freemates_android.ui.adapter.duration.DurationAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.gridview.category.CategoryAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.GridSpacingDecoration
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class DurationCourseFragment : Fragment(R.layout.fragment_duration_course) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentDurationCourseBinding.bind(view)

        val durationList = ArrayList<String>()
        durationList.add("30분 코스")
        durationList.add("1시간 코스")
        durationList.add("1시간 30분 코스")
        durationList.add("2시간 코스")
        durationList.add("2시간 30분 코스")
        durationList.add("3시간 코스")

        val durationHorizontalSpacingDecoration = HorizontalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val durationAdapter = DurationAdapter(requireContext(), durationList)
        durationAdapter.setOnItemClickListener(object : DurationAdapter.OnItemClickListener {
            override fun onItemClick(item: String) {
//                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.rvCourseDurationFilterDurationCourse.apply {
            adapter = durationAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(durationHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }

        val courseList = ArrayList<Course>()
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기1", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기2", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기3", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기4", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기5", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기6", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기7", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기8", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기9", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기10", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기11", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기12", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기13", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기14", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기15", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기16", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기17", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기18", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기19", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기20", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기21", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기22", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기23", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))

        val courseHeaderList = ArrayList<String>()
        courseHeaderList.add("파인애플농부애옹님이 좋아하는 코스!")
        courseHeaderList.add("30분 코스")
        courseHeaderList.add("1시간 코스")
        courseHeaderList.add("1시간 30분 코스")
        courseHeaderList.add("2시간 코스")
        courseHeaderList.add("2시간 30분 코스")
        courseHeaderList.add("3시간 코스")

        val courseVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 12,              // 아이템 간 간격
        )

        val courseAdapter = CourseAdapter(requireContext(), courseList, courseHeaderList, "durationCourse")
        courseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener {
            override fun onItemClick(item: Course) {
//                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.rvCourseDurationCourse.apply {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(courseVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}