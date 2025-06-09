package com.example.freemates_android

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.databinding.FragmentSearchBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.SearchItem
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.gridview.category.CategoryAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.adapter.search.SearchAdapter
import com.example.freemates_android.ui.adapter.search.SearchViewHolder
import com.example.freemates_android.ui.decoration.GridSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var pageName: String
    private var recommendList: ArrayList<RecommendItem> = ArrayList()
    private lateinit var searchList: ArrayList<String>
    private lateinit var loadingDialog: LoadingDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)

        loadingDialog = LoadingDialog(requireContext())

        pageName = requireArguments().getString("pageName").toString()
        Log.d("Search", "pageName: ${pageName}")

        viewInit()
        clickEvent()
    }

    private fun viewInit() {
        binding.clRecentSearchContainerSearch.visibility = View.VISIBLE
        binding.clSearchResultContainerSearch.visibility = View.GONE
        recentSearchRecyclerviewInit()
    }

    private fun clickEvent(){
        binding.btnSearchIconSearch.setOnClickListener {
            val searchText = binding.etSearchInputSearch.text.toString()

            if(searchText.isNotEmpty()) {
                binding.tvSearchContentSearch.text = "'$searchText'검색 결과"
                searchList.add(searchText)
                requireContext().saveStringList(searchList)
                fetchSearchData(searchText)
            } else {
                binding.clRecentSearchContainerSearch.visibility = View.VISIBLE
                binding.clSearchResultContainerSearch.visibility = View.GONE
                recentSearchRecyclerviewInit()
            }
        }
    }

    private fun fetchSearchData(keyword: String){
        loadingDialog.showLoading()

        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
            val refreshToken = requireContext().getRefreshToken()
            Log.d("Search", "refreshToken : $refreshToken")
            RetrofitClient.searchService.searchPlace(
                "Bearer $refreshToken",
                "",
                keyword,
                0,
                10
            ).enqueue(object :
                Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    Log.d("Search", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            binding.clSearchResultContainerSearch.visibility = View.VISIBLE
                            binding.clRecentSearchContainerSearch.visibility = View.GONE

                            recommendList.clear()
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

                                    Log.d("Search", "category is $selectedCategory")

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

                            loadingDialog.hideLoading()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("Search", "응답 실패: ${response.code()} - $errorCode")

                            loadingDialog.hideLoading()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("Search", value)

                    loadingDialog.hideLoading()
                }
            })
        }
    }

    private fun recentSearchRecyclerviewInit() {
        searchList = requireContext().loadStringList()

        val verticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 8
        )

        val searchAdapter = SearchAdapter(requireContext(), searchList)
        binding.rvRecentSearchTitleSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(verticalSpacingDecoration)
            setHasFixedSize(true)
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
                if (pageName == "courseEdit") {
                    showSelectDialog(item)
                } else {
                    val bundle = bundleOf("placeInfo" to item)
                    findNavController().navigate(
                        R.id.action_searchFragment_to_placeInfoFragment,
                        bundle
                    )
                }
            }
        })

        binding.rvRecommendListSearch.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }

        binding.tvSearchPlacesCntSearch.text = "${recommendList.size}개"
    }

    private fun showSelectDialog(item: RecommendItem) {
        val course = requireArguments().getParcelable<Course>("courseInfo")

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_place, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val title = dialogView.findViewById<TextView>(R.id.tvPlaceTitle_DialogSelectPlace)
        val unselect = dialogView.findViewById<TextView>(R.id.tvPlaceUnselect_DialogSelectPlace)
        val select = dialogView.findViewById<TextView>(R.id.tvPlaceSelect_DialogSelectPlace)

        title.text = "${item.placeTitle}\n코스를 선택하시겠습니까?"

        unselect.setOnClickListener {
            dialog.dismiss()
        }

        select.setOnClickListener {
            val placelst = course?.places?.toMutableList() ?: mutableListOf()
            placelst.add(item)
            course?.places = placelst

            val bundle = bundleOf("courseInfo" to course)
            findNavController().navigate(
                R.id.action_searchFragment_to_courseEditFragment,
                bundle
            )

            dialog.dismiss()
        }

        dialog.show()
    }
}