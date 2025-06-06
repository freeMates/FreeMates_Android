package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.databinding.FragmentRecommendBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.course.CourseAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class RecommendFragment : Fragment(R.layout.fragment_recommend) {
    lateinit var binding: FragmentRecommendBinding

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRecommendBinding.bind(view)

        recyclerviewInit()
        clickEvent()
    }

    private fun recyclerviewInit(){
        val courseList = ArrayList<Course>()
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기1", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기2", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기3", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
        courseList.add(Course(R.drawable.image2, "맛집 탐방하기4", "파인애플농부애옹",true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))

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
                val bundle = bundleOf("courseInfo" to item)
                findNavController().navigate(R.id.action_recommendFragment_to_courseInfoFragment, bundle)
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

        val recommendList = ArrayList<RecommendItem>()

        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2.toString(), "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", "")
        )

        val favoriteListInfo = FavoriteList(R.drawable.ic_yellow_marker, "브랫서울", "R.drawable.image2",
            "서울 광진구 광나루로 410 1층 101호", recommendList, false)

        val favoriteAdapter = FavoriteAdapter(requireContext(), favoriteList)
        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(item: FavoriteItem) {
                val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                    putExtra(ARG_FAVORITE_DETAIL, favoriteListInfo)
                }
                startActivity(intent)
//                val sourceTag = "recommend"
//                val bundle = bundleOf(
//                    FavoriteDetailSheet.ARG_FAVORITE_DETAIL to favoriteListInfo,
//                    FavoriteDetailSheet.ARG_FAVORITE_SOURCE to sourceTag
//                )
//                findNavController().navigate(R.id.action_recommendFragment_to_sheetFavoriteDetailFragment, bundle)
            }
        })

        binding.rvFavoriteListRecommend.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }


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