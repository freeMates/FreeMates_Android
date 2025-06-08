package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.UserInfoManager.getNicknameInfo
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
import kotlinx.coroutines.launch

class DurationCourseFragment : Fragment(R.layout.fragment_duration_course) {
    private lateinit var course: ArrayList<Course>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentDurationCourseBinding.bind(view)

        course = requireArguments().getParcelableArrayList<Course>("courseList") ?: arrayListOf()

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

        lifecycleScope.launch {
            val name = requireContext().getNicknameInfo()

            val courseHeaderList = ArrayList<String>()
            courseHeaderList.add("${name}님이 좋아하는 코스!")
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

            val courseAdapter = CourseAdapter(requireContext(), course, courseHeaderList, "durationCourse")
            courseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener {
                override fun onItemClick(item: Course) {
                findNavController().navigate(R.id.action_durationCourseFragment_to_courseInfoFragment)
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
}