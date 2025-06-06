package com.example.freemates_android.sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.R
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.databinding.SheetCategoryResultBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryResultSheet : Fragment() {
    companion object {
        private const val ARG_CATEGORY = "arg_category"
        private const val ARG_PLACES = "arg_places"

        fun newInstance(category: String, places: List<RecommendItem>): CategoryResultSheet {
            val fragment = CategoryResultSheet()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            args.putParcelableArrayList(ARG_PLACES, ArrayList(places))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var category: String
    private var places = ArrayList<RecommendItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY)!!
//            places = it.getParcelableArrayList(ARG_PLACES)!!
        }
    }

    private lateinit var binding: SheetCategoryResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SheetCategoryResultBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정

        Log.d("CategoryResultSheet", "category is $category")
        getData(category)

        return binding.root
    }

    private fun getData(category: String){
        Log.d("Category", "categoryName : $category")
        places.clear()

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

                                // TODO API 수정되면 items.distance 넣기
                                val item = RecommendItem(
                                    items.imageUrl,
                                    items.placeName,
                                    false,
                                    items.likeCount,
                                    items.addressName,
                                    selectedIconRes,
                                    selectedCategory,
                                    items.tags,
                                    items.introText,
                                    "743"
                                )
                                places.add(item)
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
            context = requireContext(),
            spacingDp = 8,
        )

        val recommendAdapter = RecommendAdapter(requireContext(), places)
        recommendAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
            override fun onItemClick(item: RecommendItem) {
                val bundle = bundleOf("placeInfo" to item)
                findNavController().navigate(R.id.action_mapFragment_to_placeInfoFragment, bundle)
            }
        })

        val categoryText: String =
            when (category.uppercase()) {
                "CAFE" -> "카페"
                "FOOD" -> "먹거리"
                "SHOPPING" -> "쇼핑"
                "WALK" -> "산책"
                "PLAY" -> "놀거리"
                "HOSPITAL" -> "병원"
                else -> "카페"
            }
        binding.tvPlaceCategoryPromptCategoryResult.text = categoryText

        binding.rvPlaceListCategoryResult.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val hasVerticalSpacingDecoration = this.itemDecorationCount > 0 &&
                    (0 until this.itemDecorationCount).any { index ->
                        this.getItemDecorationAt(index) is VerticalSpacingDecoration
                    }
            if (!hasVerticalSpacingDecoration) {
                this.addItemDecoration(recommendVerticalSpacingDecoration)
            }
            setHasFixedSize(true)
        }
    }
}