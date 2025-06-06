package com.example.freemates_android.sheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.HeartOverlayUtil
import com.example.freemates_android.R
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.MyBookmarkListResponse
import com.example.freemates_android.api.dto.PlaceDto
import com.example.freemates_android.databinding.SheetAddFavoritePlaceBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.AddFavorite
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.AddFavoriteAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFavoritePlaceSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SheetAddFavoritePlaceBinding
    val IMAGE_BASE_URL = "http://3.34.78.124:8087"

    private lateinit var recommendList: ArrayList<RecommendItem>
//        listOf(
//        RecommendItem(
//            R.drawable.image2.toString(), "브랫서울", true, 1345,
//            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
//            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
//        RecommendItem(
//            R.drawable.image2.toString(), "브랫서울", true, 1345,
//            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
//            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
//        RecommendItem(
//            R.drawable.image2.toString(), "브랫서울", true, 1345,
//            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
//            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
//        RecommendItem(
//            R.drawable.image2.toString(), "브랫서울", true, 1345,
//            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
//            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
//        RecommendItem(
//            R.drawable.image2.toString(), "브랫서울", true, 1345,
//            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
//            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
//    )

    private lateinit var favoriteList: ArrayList<AddFavorite>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = (it as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                view.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.roundedbox_top_20 // 네가 설정한 drawable 파일
                )
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SheetAddFavoritePlaceBinding.inflate(inflater, container, false)

        fetchBookmarkList()
        setupRecyclerView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fetchBookmarkList()
    }

    private fun fetchBookmarkList(){
        viewLifecycleOwner.lifecycleScope.launch {
            val userNickname: String = requireContext().getNicknameInfo()
            val refreshToken = requireContext().getRefreshToken()  // 필요 시
            RetrofitClient.bookmarkService.myBookmarkList(
                "Bearer $refreshToken",
            ).enqueue(object :
                Callback<List<MyBookmarkListResponse>> {
                override fun onResponse(
                    call: Call<List<MyBookmarkListResponse>>,
                    response: Response<List<MyBookmarkListResponse>>
                ) {
                    Log.d("AddFavoritePlace", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            val tempList = mutableListOf<AddFavorite>()
                            response.body()?.forEach { items ->
                                fetchBookmarkPlaces(items)
                            }
//                            favoriteList = tempList.toList()

                            setupRecyclerView()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("AddFavoritePlace", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }
                }

                override fun onFailure(call: Call<List<MyBookmarkListResponse>>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("AddFavoritePlace", value)
                }
            })
        }
    }

    private fun fetchBookmarkPlaces(favoriteItems: MyBookmarkListResponse){
        // TODO API를 못 찾음
        //  API 오류
        Log.d("AddFavoritePlace", "bookmarkId : ${favoriteItems.bookmarkId}")

        viewLifecycleOwner.lifecycleScope.launch {
            val userNickname: String = requireContext().getNicknameInfo()
            val refreshToken = requireContext().getRefreshToken()  // 필요 시
            favoriteItems.bookmarkId?.let {
                RetrofitClient.bookmarkService.getBookmarkPlaces(
                    "Bearer $refreshToken",
                    it
                ).enqueue(object :
                    Callback<List<PlaceDto>> {
                    override fun onResponse(
                        call: Call<List<PlaceDto>>,
                        response: Response<List<PlaceDto>>
                    ) {
                        Log.d("AddFavoritePlace", "response code :${response.code()}")
                        when (response.code()) {
                            200 -> {
                                recommendList.clear()
                                response.body()?.forEach { items ->
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

                                    recommendList.add(RecommendItem(items.imageUrl, items.placeName, false, items.likeCount, items.addressName, selectedIconRes, selectedCategory, items.tags, items.introText, "743"))
                                }

                                val pinColorRes: Int = when (favoriteItems.pinColor.uppercase()) {
                                    "RED" -> R.drawable.ic_red_marker
                                    "YELLOW" -> R.drawable.ic_yellow_marker
                                    "GREEN" -> R.drawable.ic_green_marker
                                    "BLUE" -> R.drawable.ic_blue_marker
                                    "PURPLE" -> R.drawable.ic_purple_marker
                                    "PINK" -> R.drawable.ic_pink_marker
                                    else -> R.drawable.ic_red_marker
                                }
                                val visibilityStatus: Boolean =
                                    favoriteItems.visibility.uppercase() == "PUBLIC"

                                val imageUrl = IMAGE_BASE_URL + favoriteItems.imageUrl

                                val item = AddFavorite(
                                    pinColorRes,
                                    favoriteItems.title,
                                    imageUrl,
                                    recommendList,
                                    visibilityStatus
                                )

                                if (favoriteItems.nickname == userNickname) {
                                    favoriteList.add(item)
                                }
                            }

                            else -> {
                                val errorCode = response.errorBody()?.string()
                                Log.e("AddFavoritePlace", "응답 실패: ${response.code()} - $errorCode")
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<PlaceDto>>, t: Throwable) {
                        val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                        Log.d("AddFavoritePlace", value)
                    }
                })
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = AddFavoriteAdapter(requireContext(), emptyList()) {
            // 헤더 클릭 시 (즐겨찾기 목록 추가 클릭)
            showAddFavoriteDialog()
        }
        binding.rvFavoriteListAddFavoritePlace.adapter = adapter
        binding.rvFavoriteListAddFavoritePlace.layoutManager = LinearLayoutManager(context)

        adapter.setOnItemClickListener {
            HeartOverlayUtil.showHeartOverlay(requireContext())
            this.dismiss()
        }
    }

    val favoriteListInfo = FavoriteList(
        R.drawable.ic_red_marker, "", "R.drawable.image1",
        "", emptyList(), true)

    // Header 부분
    private fun showAddFavoriteDialog() {
        // 여기에 즐겨찾기 추가 Dialog 구현
        val intent = Intent(requireContext(), EditFavoriteActivity::class.java).apply {
            putExtra(ARG_FAVORITE_DETAIL, favoriteListInfo)
            putExtra(ARG_PAGE_NAME, "addFavoritePlace")
        }
        startActivity(intent)
    }

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        private const val ARG_PAGE_NAME = "arg_page_name"
    }
}