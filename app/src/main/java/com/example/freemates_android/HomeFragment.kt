package com.example.freemates_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.gridview.category.CategoryAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.GridSpacingDecoration
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHomeBinding.bind(view)

        val categoryList = ArrayList<CategoryItem>()
        categoryList.add(CategoryItem(R.drawable.ic_category_cafe_off, "카페"))
        categoryList.add(CategoryItem(R.drawable.ic_category_leisure_off, "놀거리"))
        categoryList.add(CategoryItem(R.drawable.ic_category_walk_off, "산책"))
        categoryList.add(CategoryItem(R.drawable.ic_category_foods_off, "먹거리"))
        categoryList.add(CategoryItem(R.drawable.ic_category_hospital_off, "병원"))
        categoryList.add(CategoryItem(R.drawable.ic_category_shopping_off, "쇼핑"))

        val spacingDecoration = GridSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spanCount = 3,              // 예: 3열
            spacingDp = 8,              // 아이템 간 간격
            includeEdge = true          // 양쪽 간격 포함 여부
        )

        binding.rvCategoryHome.apply {
            adapter = this@HomeFragment.context?.let { CategoryAdapter(it, categoryList) }
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(spacingDecoration)
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

        binding.rvFavoriteListHome.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }

        val recommendList = ArrayList<RecommendItem>()
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))
        recommendList.add(RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요"))))


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

        binding.rvRecommendListHome.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}