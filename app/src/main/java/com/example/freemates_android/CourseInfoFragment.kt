package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.FragmentCourseInfoBinding
import com.example.freemates_android.databinding.FragmentPlaceInfoBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.adapter.course.CourseAdapter
import com.example.freemates_android.ui.adapter.course.CourseInfoAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import com.google.android.flexbox.FlexboxLayout

class CourseInfoFragment : Fragment(R.layout.fragment_course_info) {

    private lateinit var binding: FragmentCourseInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCourseInfoBinding.bind(view)

        initUI()
        recyclerviewInit()
        clickEvent()
    }

    private fun initUI(){
        val course = requireArguments().getParcelable<Course>("courseInfo")

        if (course != null) {
            // creator랑 현재 아이디랑 똑같은지 비교
//            if()
            binding.btnCourseEditCourseInfo.visibility = View.VISIBLE

            Glide.with(this)
                .load(course.courseImage)
                .into(binding.ivCourseImageCourseInfo)
            if(course.visibilityStatus)
                binding.ivCourseVisibilityStatusCourseInfo.visibility = View.GONE
            else
                binding.ivCourseVisibilityStatusCourseInfo.visibility = View.VISIBLE
            binding.tvCourseTitleCourseInfo.text = course.courseTitle
            binding.tvCourseCreatorCourseInfo.text = "@${course.courseCreator}"
            binding.tvCourseDurationCourseInfo.text = course.courseDuration
            binding.tvCourseDescriptionCourseInfo.text = course.courseDescription
            binding.btnLikeCourseInfo.isSelected = course.like
            binding.tvLikeCntCourseInfo.text = course.likeCnt.toString()


        }
    }

    private fun recyclerviewInit(){
        val courseInfoList = ArrayList<CourseInfo>()
        courseInfoList.add(CourseInfo("스시붐", 11, 0, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(FilterItem("혼자서도 좋아요")), R.drawable.image2))
        courseInfoList.add(CourseInfo("스시붐", 5, 3, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(FilterItem("혼자서도 좋아요")), R.drawable.image2))
        courseInfoList.add(CourseInfo("스시붐", 3, 0, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(FilterItem("혼자서도 좋아요")), R.drawable.image2))
        courseInfoList.add(CourseInfo("스시붐", 11, 2, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(FilterItem("혼자서도 좋아요")), R.drawable.image2))

        val courseInfoAdapter = CourseInfoAdapter(requireContext(), courseInfoList)
//        courseInfoAdapter.setOnItemClickListener(object : CourseInfoAdapter.OnItemClickListener {
//            override fun onItemClick(item: Course) {
//                val bundle = bundleOf("courseInfo" to item)
//                findNavController().navigate(R.id.action_recommendFragment_to_courseInfoFragment, bundle)
//            }
//        })

        binding.rvCoursePlaceCourseInfo.apply {
            adapter = courseInfoAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun clickEvent(){
        binding.btnBackToRecommendCourseInfo.setOnClickListener {
            findNavController().navigate(R.id.action_courseInfoFragment_to_recommendFragment)
        }
    }
}