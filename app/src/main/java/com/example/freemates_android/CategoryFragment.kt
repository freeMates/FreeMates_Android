package com.example.freemates_android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.databinding.FragmentCategoryBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var binding: FragmentCategoryBinding
    private val recommendList = ArrayList<RecommendItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoryBinding.bind(view)

        var category = requireArguments().getString("categoryTitle").toString()

        val categoryList = arrayListOf(
            CategoryItem(R.drawable.ic_cafe_large_on, R.drawable.ic_cafe_large_off, "카페", false),
            CategoryItem(R.drawable.ic_leisure_large_on,R.drawable.ic_leisure_large_off, "놀거리", false),
            CategoryItem(R.drawable.ic_walk_large_on, R.drawable.ic_walk_large_off, "산책", false),
            CategoryItem(R.drawable.ic_foods_large_on, R.drawable.ic_foods_large_off, "먹거리", false),
            CategoryItem(R.drawable.ic_hospital_large_on, R.drawable.ic_hospital_large_off, "병원", false),
            CategoryItem(R.drawable.ic_shopping_large_on, R.drawable.ic_shopping_large_off, "쇼핑", false),
        )

        for (item in categoryList){
            if(item.title == category) {
                item.isSelected = true
            }
        }

        category =
            when (category) {
                "카페" -> "CAFE"
                "먹거리" -> "FOOD"
                "쇼핑" -> "SHOPPING"
                "산책" -> "WALK"
                "놀거리" -> "PLAY"
                "병원" -> "HOSPITAL"
                else -> ""
            }

        Log.d("Category", "category is ${category}")

        val spacingDecoration = HorizontalSpacingDecoration(requireContext(), 8)
        val adapter = CategoryLargeAdapter(requireContext(), categoryList) { categoryTitle ->
            getData(categoryTitle)
        }

        binding.rvPlaceCategoryCategory.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(spacingDecoration)
            setHasFixedSize(true)
        }

        binding.tvPlaceListCntCategory.text = "${recommendList.size}개"

        getData(category)
    }

    private fun getData(category: String){
        Log.d("Category", "categoryName : ${category}")
        recommendList.clear()

        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
            val refreshToken = requireContext().getRefreshToken()
            Log.d("Category", "refreshToken : $refreshToken")
            RetrofitClient.placeService.placeCategory(
                "Bearer $refreshToken",
                category,
                0,
                10
            ).enqueue(object :
                Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    Log.d("Category", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                                for (items in response.body()!!.content) {
                                    val selectedIconRes: Int =
                                        when (items.categoryType.uppercase()) {
                                            "CAFE" -> R.drawable.ic_cafe_small_on
                                            "FOOD" -> R.drawable.ic_foods_small_on
                                            "SHOPPING" -> R.drawable.ic_shopping_small_on
                                            "WALK" -> R.drawable.ic_walk_small_on
                                            "PLAY" -> R.drawable.ic_leisure_small_on
                                            "HOSPITAL" -> R.drawable.ic_hospital_small_on
                                            else -> R.drawable.ic_cafe_small_on
                                        }
                                    val selectedCategory: String =
                                        when (items.categoryType.uppercase()) {
                                            "CAFE" -> "카페"
                                            "FOOD" -> "먹거리"
                                            "SHOPPING" -> "쇼핑"
                                            "WALK" -> "산책"
                                            "PLAY" -> "놀거리"
                                            "HOSPITAL" -> "병원"
                                            else -> "카페"
                                        }

                                    val item = RecommendItem(
                                        items.placeId,
                                        items.imageUrl,
                                        items.placeName,
                                        false,
                                        items.likeCount,
                                        items.addressName,
                                        selectedIconRes,
                                        selectedCategory,
                                        items.tags,
                                        items.introText,
                                        items.distance
                                    )
                                    recommendList.add(item)
                                }
                            recommendRecyclerviewInit()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)
                }
            })
        }
    }

    private fun recommendRecyclerviewInit(){
        val recommendVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val recommendAdapter = RecommendAdapter(requireContext(), recommendList)
        recommendAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
            override fun onItemClick(item: RecommendItem) {
                val bundle = bundleOf("placeInfo" to item)
                findNavController().navigate(R.id.action_categoryFragment_to_placeInfoFragment, bundle)
            }
        })

        binding.rvRecommendListCategory.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            // 3-1) 이미 동일한 타입의 Decoration이 붙어 있는지 체크
            val hasVerticalSpacingDecoration = this.itemDecorationCount > 0 &&
                    (0 until this.itemDecorationCount).any { index ->
                        this.getItemDecorationAt(index) is VerticalSpacingDecoration
                    }

            // 3-2) 만약 없다면 추가
            if (!hasVerticalSpacingDecoration) {
                this.addItemDecoration(recommendVerticalSpacingDecoration)
            }
            setHasFixedSize(true)
        }

        binding.tvPlaceListCntCategory.text = "${recommendList.size}개"
    }
}