package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.FragmentCourseEditBinding
import com.example.freemates_android.databinding.FragmentCourseInfoBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.ui.adapter.course.CourseEditAdapter
import com.example.freemates_android.ui.adapter.course.CourseInfoAdapter

class CourseEditFragment : Fragment(R.layout.fragment_course_edit) {

    private lateinit var binding: FragmentCourseEditBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCourseEditBinding.bind(view)

        initUI()
        recyclerviewInit()
        clickEvent()

        binding.etCourseDescriptionCourseEdit.doOnTextChanged { text, _, _, _ ->
            binding.tvCourseDescriptionLengthCourseEdit.text = "${text?.length ?: 0} / 60"
        }
    }

    private fun initUI(){
        val course = requireArguments().getParcelable<Course>("courseInfo")

        if (course != null) {
            Glide.with(this)
                .load(course.courseImage)
                .into(binding.ivCourseImageCourseEdit)
            binding.etCourseTitleCourseEdit.setText(course.courseTitle)

            binding.btnCourseDuration30minCourseEdit.isSelected = false
            binding.btnCourseDuration60minCourseEdit.isSelected = false
            binding.btnCourseDuration90minCourseEdit.isSelected = false
            binding.btnCourseDuration120minCourseEdit.isSelected = false
            binding.btnCourseDuration150minCourseEdit.isSelected = false
            binding.btnCourseDuration180minCourseEdit.isSelected = false

            if (course.courseDuration == "30분 소요 코스")
                binding.btnCourseDuration30minCourseEdit.isSelected = true
            else if (course.courseDuration == "1시간 소요 코스")
                binding.btnCourseDuration60minCourseEdit.isSelected = true
            else if (course.courseDuration == "1시간 30분 소요 코스")
                binding.btnCourseDuration90minCourseEdit.isSelected = true
            else if (course.courseDuration == "2시간 소요 코스")
                binding.btnCourseDuration120minCourseEdit.isSelected = true
            else if (course.courseDuration == "2시간 30분 소요 코스")
                binding.btnCourseDuration150minCourseEdit.isSelected = true
            else if (course.courseDuration == "3시간 소요 코스")
                binding.btnCourseDuration180minCourseEdit.isSelected = true

            binding.etCourseDescriptionCourseEdit.setText(course.courseDescription)

            binding.btnCourseVisibilityPublicCourseEdit.isSelected = false
            binding.btnCourseVisibilityPrivateCourseEdit.isSelected = false

            if (course.visibilityStatus)
                binding.btnCourseVisibilityPublicCourseEdit.isSelected = true
            else
                binding.btnCourseVisibilityPrivateCourseEdit.isSelected = true
        }
    }

    private fun recyclerviewInit(){
        val courseInfoList = ArrayList<CourseInfo>()
        courseInfoList.add(
            CourseInfo("스시붐", 11, 0, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(
                "혼자서도 좋아요"
            ), R.drawable.image2)
        )
        courseInfoList.add(
            CourseInfo("스시붐", 5, 3, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(
                "혼자서도 좋아요"
            ), R.drawable.image2)
        )
        courseInfoList.add(
            CourseInfo("스시붐", 3, 0, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(
                "혼자서도 좋아요"
            ), R.drawable.image2)
        )
        courseInfoList.add(
            CourseInfo("스시붐", 11, 2, listOf(CategoryItem(R.drawable.ic_category_cafe, "카페")), listOf(
                "혼자서도 좋아요"
            ), R.drawable.image2)
        )

        val courseEditAdapter = CourseEditAdapter(requireContext(), courseInfoList)
//        courseInfoAdapter.setOnItemClickListener(object : CourseInfoAdapter.OnItemClickListener {
//            override fun onItemClick(item: Course) {
//                val bundle = bundleOf("courseInfo" to item)
//                findNavController().navigate(R.id.action_recommendFragment_to_courseInfoFragment, bundle)
//            }
//        })

        binding.rvCoursePlaceCourseEdit.apply {
            adapter = courseEditAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun clickEvent(){
        binding.btnBackToCourseInfoCourseEdit.setOnClickListener {
            findNavController().navigate(R.id.action_courseEditFragment_to_courseInfoFragment)
        }
    }
}