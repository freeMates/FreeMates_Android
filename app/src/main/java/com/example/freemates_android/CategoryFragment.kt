package com.example.freemates_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentCategoryBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class CategoryFragment : Fragment(R.layout.fragment_category) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentCategoryBinding.bind(view)

        val recommendList = ArrayList<RecommendItem>()
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )
        recommendList.add(
            RecommendItem(
                R.drawable.image2, "브랫서울", true, 1345,
                "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
                listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))
            )
        )

        val categoryList = arrayListOf(
            CategoryItem(R.drawable.ic_cafe_large_on, R.drawable.ic_cafe_large_off, "카페", true),
            CategoryItem(R.drawable.ic_leisure_large_on,R.drawable.ic_leisure_large_off, "놀거리", false),
            CategoryItem(R.drawable.ic_walk_large_on, R.drawable.ic_walk_large_off, "산책", false),
            CategoryItem(R.drawable.ic_foods_large_on, R.drawable.ic_foods_large_off, "먹거리", false),
            CategoryItem(R.drawable.ic_hospital_large_on, R.drawable.ic_hospital_large_off, "병원", false),
            CategoryItem(R.drawable.ic_shopping_large_on, R.drawable.ic_shopping_large_off, "쇼핑", false),
        )

        val spacingDecoration = HorizontalSpacingDecoration(requireContext(), 8)
        val adapter = CategoryLargeAdapter(requireContext(), categoryList) { _ ->
//            viewModel.fetchPlacesByCategory(categoryName)
        }

        binding.rvPlaceCategoryCategory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(spacingDecoration)
            setHasFixedSize(true)
        }

        binding.tvPlaceListCntCategory.text = "${recommendList.size}개"

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

        binding.rvRecommendListCategory.apply {
            this.adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}