package com.example.freemates_android

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.FragmentCourseInfoBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.ui.adapter.course.CourseInfoAdapter

class CourseInfoFragment : Fragment(R.layout.fragment_course_info) {

    private lateinit var binding: FragmentCourseInfoBinding
    private lateinit var course: Course

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCourseInfoBinding.bind(view)

        initUI()
        recyclerviewInit()
        clickEvent()
    }

    private fun initUI(){
        course = requireArguments().getParcelable<Course>("courseInfo")!!

        if (course != null) {
            // creator랑 현재 아이디랑 똑같은지 비교
//            if()
            binding.btnCourseEditCourseInfo.visibility = View.VISIBLE

            binding.btnCourseEditCourseInfo.setOnClickListener {
                val bundle = bundleOf("courseInfo" to course)
                findNavController().navigate(R.id.action_courseInfoFragment_to_courseEditFragment, bundle)
            }

            Glide.with(this)
                .load(course.courseImage)
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
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
        val courseInfoList = course.places
//            ArrayList<CourseInfo>()
//        courseInfoList.add(CourseInfo("스시붐", 11, 0, listOf(Category(R.drawable.ic_category_cafe, "카페")), listOf("혼자서도 좋아요"), R.drawable.image2))
//        courseInfoList.add(CourseInfo("스시붐", 5, 3, listOf(Category(R.drawable.ic_category_cafe, "카페")), listOf("혼자서도 좋아요"), R.drawable.image2))
//        courseInfoList.add(CourseInfo("스시붐", 3, 0, listOf(Category(R.drawable.ic_category_cafe, "카페")), listOf("혼자서도 좋아요"), R.drawable.image2))
//        courseInfoList.add(CourseInfo("스시붐", 11, 2, listOf(Category(R.drawable.ic_category_cafe, "카페")),listOf("혼자서도 좋아요"), R.drawable.image2))

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