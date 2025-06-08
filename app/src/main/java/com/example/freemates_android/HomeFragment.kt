package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.api.dto.PageBookmarkResponse
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.gridview.category.CategoryAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.GridSpacingDecoration
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    val recommendList = ArrayList<RecommendItem>()
    private val favoriteList: ArrayList<FavoriteList> = ArrayList<FavoriteList>()

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)

        initUI()
        getRecommendData()
        getBookmarkData()
        recommendRecyclerviewInit()
    }

    private fun initUI(){
        val categoryList = ArrayList<Category>()
        categoryList.add(Category(R.drawable.ic_category_cafe_off, "카페"))
        categoryList.add(Category(R.drawable.ic_category_leisure_off, "놀거리"))
        categoryList.add(Category(R.drawable.ic_category_walk_off, "산책"))
        categoryList.add(Category(R.drawable.ic_category_foods_off, "먹거리"))
        categoryList.add(Category(R.drawable.ic_category_hospital_off, "병원"))
        categoryList.add(Category(R.drawable.ic_category_shopping_off, "쇼핑"))

        val spacingDecoration = GridSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spanCount = 3,              // 예: 3열
            spacingDp = 8,              // 아이템 간 간격
            includeEdge = true          // 양쪽 간격 포함 여부
        )

        val categoryAdapter = CategoryAdapter(requireContext(), categoryList)
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(item: Category) {
                val bundle = bundleOf("categoryTitle" to item.categoryTitle)
                findNavController().navigate(R.id.action_homeFragment_to_categoryFragment, bundle)
            }
        })

        binding.rvCategoryHome.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(spacingDecoration)
            setHasFixedSize(true)
        }
    }

    private fun getRecommendData(){
        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
            val refreshToken = requireContext().getRefreshToken()
            Log.d("Home", "refreshToken : ${refreshToken}")
            RetrofitClient.placeService.placeCategory(
                "Bearer $refreshToken",
                "",
                0,
                10
            ).enqueue(object :
                Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    Log.d("Home", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.empty == false) {
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
                                            else -> ""
                                        }

                                    Log.d("Home", "category is $selectedCategory")

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
                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment, bundle)
            }
        })

        binding.rvRecommendListHome.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }

    val IMAGE_BASE_URL = "http://3.34.78.124:8087"
    private fun getBookmarkData(){
        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
            val refreshToken = requireContext().getRefreshToken()
            Log.d("Home", "refreshToken : $refreshToken")
            RetrofitClient.bookmarkService.getBookmarkList(
                "Bearer $refreshToken",
                0,
                10,
                "PUBLIC"
            ).enqueue(object :
                Callback<PageBookmarkResponse> {
                override fun onResponse(
                    call: Call<PageBookmarkResponse>,
                    response: Response<PageBookmarkResponse>
                ) {
                    Log.d("Home", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.empty == false) {
                                for (items in response.body()!!.content) {
                                    val imageUrl = IMAGE_BASE_URL + items.imageUrl

                                    val selectedPin: Int =
                                        when (items.pinColor.uppercase()) {
                                            "RED" -> R.drawable.ic_red_marker
                                            "YELLOW" -> R.drawable.ic_yellow_marker
                                            "GREEN" -> R.drawable.ic_green_marker
                                            "BLUE" -> R.drawable.ic_blue_marker
                                            "PURPLE" -> R.drawable.ic_purple_marker
                                            "PINK" -> R.drawable.ic_pink_marker
                                            else -> R.drawable.ic_red_marker
                                        }

                                    val recommendList: ArrayList<RecommendItem> = ArrayList()
                                    if(!items.placeDtos.isNullOrEmpty()) {
                                        for (placeItem in items.placeDtos) {
                                            recommendList.add(
                                                RecommendItem(
                                                    placeItem.placeId,
                                                    placeItem.imageUrl,
                                                    placeItem.placeName,
                                                    false,
                                                    placeItem.likeCount,
                                                    placeItem.addressName,
                                                    when (placeItem.categoryType.uppercase()) {
                                                        "CAFE" -> R.drawable.ic_cafe_small_on
                                                        "FOOD" -> R.drawable.ic_foods_small_on
                                                        "SHOPPING" -> R.drawable.ic_shopping_small_on
                                                        "WALK" -> R.drawable.ic_walk_small_on
                                                        "PLAY" -> R.drawable.ic_leisure_small_on
                                                        "HOSPITAL" -> R.drawable.ic_hospital_small_on
                                                        else -> R.drawable.ic_cafe_small_on
                                                    },
                                                    when (placeItem.categoryType.uppercase()) {
                                                        "CAFE" -> "카페"
                                                        "FOOD" -> "먹거리"
                                                        "SHOPPING" -> "쇼핑"
                                                        "WALK" -> "산책"
                                                        "PLAY" -> "놀거리"
                                                        "HOSPITAL" -> "병원"
                                                        else -> ""
                                                    },
                                                    placeItem.tags,
                                                    placeItem.introText,
                                                    placeItem.distance
                                                )
                                            )
                                        }
                                    }

                                    val tmpList = items.bookmarkId?.let {
                                        FavoriteList(
                                            selectedPin,
                                            items.title,
                                            imageUrl,
                                            items.description,
                                            recommendList,
                                            items.visibility == "PUBLIC",
                                            items.nickname,
                                            it
                                        )
                                    }

                                    if (tmpList != null) {
                                        favoriteList.add(tmpList)
                                    }
                                }


                            }
                            bookmarkRecyclerviewInit()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }
                }

                override fun onFailure(call: Call<PageBookmarkResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)
                }
            })
        }
    }

    private fun bookmarkRecyclerviewInit(){
        val favoriteHorizontalSpacingDecoration = HorizontalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val favoriteAdapter = FavoriteAdapter(requireContext(), favoriteList)
        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(item: FavoriteList) {
                val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                    putExtra(ARG_FAVORITE_DETAIL, item)
                }
                startActivity(intent)
            }
        })

        binding.rvFavoriteListHome.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}