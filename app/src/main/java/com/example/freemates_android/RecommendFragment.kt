package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentRecommendBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.course.CourseAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class RecommendFragment : Fragment(R.layout.fragment_recommend) {

    lateinit var binding: FragmentRecommendBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRecommendBinding.bind(view)

        recyclerviewInit()
        clickEvent()
    }

    private fun recyclerviewInit(){
        val courseList = ArrayList<Course>()
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기1", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(CategoryItem(R.drawable.ic_category_cafe, "카페"), CategoryItem(R.drawable.ic_category_sports, "스포츠"))))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기2", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(CategoryItem(R.drawable.ic_category_cafe, "카페"), CategoryItem(R.drawable.ic_category_sports, "스포츠"))))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기3", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(CategoryItem(R.drawable.ic_category_cafe, "카페"), CategoryItem(R.drawable.ic_category_sports, "스포츠"))))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기4", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(CategoryItem(R.drawable.ic_category_cafe, "카페"), CategoryItem(R.drawable.ic_category_sports, "스포츠"))))

        val courseVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 12,              // 아이템 간 간격
        )

        val courseHeaderList = ArrayList<String>()
        courseHeaderList.add("파인애플농부애옹님이 좋아하는 코스!")
        courseHeaderList.add("30분 소요 코스")
        courseHeaderList.add("1시간 소요 코스")
        courseHeaderList.add("1시간 30분 소요 코스")
        courseHeaderList.add("2시간 소요 코스")
        courseHeaderList.add("2시간 30분 소요 코스")
        courseHeaderList.add("3시간 소요 코스")

        val courseAdapter = CourseAdapter(requireContext(), courseList, courseHeaderList, "recommend")
        courseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener {
            override fun onItemClick(item: Course) {
//                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.rvPopularCourseRecommend.apply {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(courseVerticalSpacingDecoration)
            setHasFixedSize(true)
        }

        val favoriteList = ArrayList<FavoriteItem>()
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))
        favoriteList.add(FavoriteItem(R.drawable.image1, "카공이 필요할 때 카공이 필요할 때", "파인애플농부애옹"))

        val favoriteHorizontalSpacingDecoration = HorizontalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val favoriteAdapter = FavoriteAdapter(requireContext(), favoriteList)
        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(item: FavoriteItem) {
                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.rvFavoriteListRecommend.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }

        val recommendList = ArrayList<RecommendItem>()
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )
        recommendList.add(
            RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
                listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요")))
        )


        val recommendVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val recommendAdapter = RecommendAdapter(requireContext(), recommendList)
        recommendAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
            override fun onItemClick(item: RecommendItem) {
                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.rvPlaceListRecommend.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }

    private fun clickEvent(){
        binding.tvPopularCourseDetailRecommend.setOnClickListener {
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment)
        }

        binding.ivAdsRecommend.setOnClickListener{
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment)
        }
    }
}