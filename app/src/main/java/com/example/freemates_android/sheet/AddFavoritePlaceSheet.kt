package com.example.freemates_android.sheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.HeartOverlayUtil
import com.example.freemates_android.LoadingDialog
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
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.favorite.AddFavoriteAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class AddFavoritePlaceSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SheetAddFavoritePlaceBinding
    val IMAGE_BASE_URL = "http://3.34.78.124:8087"

    private var favoriteList: ArrayList<AddFavorite> = ArrayList()
    private lateinit var loadingDialog: LoadingDialog

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

    private lateinit var placeId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SheetAddFavoritePlaceBinding.inflate(inflater, container, false)
        placeId = arguments?.getString("placeId").toString()
        loadingDialog = LoadingDialog(requireContext())
//        fetchBookmarkList()
//        setupRecyclerView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        Log.d("AddFavoritePlace", "onResume 들어옴")
        fetchBookmarkList()
    }

    private fun fetchBookmarkList() {
        loadingDialog.showLoading()

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteList.clear()
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
//                                fetchBookmarkPlaces(items)

                                val pinColorRes: Int = when (items.pinColor.uppercase()) {
                                    "RED" -> R.drawable.ic_red_marker
                                    "YELLOW" -> R.drawable.ic_yellow_marker
                                    "GREEN" -> R.drawable.ic_green_marker
                                    "BLUE" -> R.drawable.ic_blue_marker
                                    "PURPLE" -> R.drawable.ic_purple_marker
                                    "PINK" -> R.drawable.ic_pink_marker
                                    else -> R.drawable.ic_red_marker
                                }
                                val visibilityStatus: Boolean =
                                    items.visibility.uppercase() == "PUBLIC"

                                val imageUrl = IMAGE_BASE_URL + items.imageUrl

                                Log.d("AddFavoritePlace", "items.placeDto : ${items.placeDtos}")
                                Log.d(
                                    "AddFavoritePlace",
                                    "items.placeDto : ${items.placeDtos?.size}"
                                )

                                val placeId: String = ""

                                val placeList: ArrayList<RecommendItem> =
                                    if (items.placeDtos != null) {
                                        val list = ArrayList<RecommendItem>()
                                        for (placeItem in items.placeDtos) {
                                            list.add(
                                                RecommendItem(
                                                    placeItem.placeId,
                                                    placeItem.imageUrl,
                                                    placeItem.placeName,
                                                    true,
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
                                                    formatMinutes(minutesFromDistance(placeItem.distance.toDouble()))
                                                )
                                            )
                                        }
                                        list
                                    } else {
                                        // 빈 리스트
                                        arrayListOf()
                                    }

                                val item = AddFavorite(
                                    pinColorRes,
                                    items.title,
                                    imageUrl,
                                    placeList,
                                    visibilityStatus,
                                    items.bookmarkId
                                )

                                favoriteList.add(item)
                            }

                            setupRecyclerView()

                            loadingDialog.hideLoading()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("AddFavoritePlace", "응답 실패: ${response.code()} - $errorCode")

                            loadingDialog.hideLoading()
                        }
                    }
                }

                override fun onFailure(call: Call<List<MyBookmarkListResponse>>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("AddFavoritePlace", value)

                    loadingDialog.hideLoading()
                }
            })
        }
    }

    private fun setupRecyclerView() {
        val adapter = AddFavoriteAdapter(requireContext(), favoriteList) {
            // 헤더 클릭 시 (즐겨찾기 목록 추가 클릭)
            showAddFavoriteDialog()
        }
        binding.rvFavoriteListAddFavoritePlace.adapter = adapter
        binding.rvFavoriteListAddFavoritePlace.layoutManager = LinearLayoutManager(context)

        adapter.setOnItemClickListener { selectedFavorite ->
            addPlaceInBookmark(selectedFavorite.bookmarkId, this)
        }
    }

    private fun addPlaceInBookmark(bookmarkId: String?, addFavoritePlaceSheet: AddFavoritePlaceSheet){
        loadingDialog.showLoading()

        Log.d("AddFavoritePlace", "bookmark : $bookmarkId")
        Log.d("AddFavoritePlace", "placeId : $placeId")
        viewLifecycleOwner.lifecycleScope.launch {
            val refreshToken = requireContext().getRefreshToken()  // 필요 시
            RetrofitClient.bookmarkService.addBookmarkPlace(
                "Bearer $refreshToken",
                bookmarkId!!,
                placeId
            ).enqueue(object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    Log.d("AddFavoritePlace", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            Toast.makeText(requireContext(), "추가 완료", Toast.LENGTH_SHORT).show()

                            HeartOverlayUtil.showHeartOverlay(requireContext())
                            addFavoritePlaceSheet.dismiss()

                            loadingDialog.hideLoading()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("AddFavoritePlace", "응답 실패: ${response.code()} - $errorCode")

                            loadingDialog.hideLoading()
                        }
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("AddFavoritePlace", value)

                    loadingDialog.hideLoading()
                }
            })
        }
    }

    private val favoriteListInfo = FavoriteList(
        R.drawable.ic_red_marker, "", "R.drawable.image1",
        "", emptyList(), true, "", ""
    )

    // Header 부분
    private fun showAddFavoriteDialog() {
        // 여기에 즐겨찾기 추가 Dialog 구현
        val intent = Intent(requireContext(), EditFavoriteActivity::class.java).apply {
            putExtra(ARG_FAVORITE_DETAIL, favoriteListInfo)
            putExtra(ARG_PAGE_NAME, "addFavoritePlace")
        }
        startActivity(intent)
    }

    fun minutesFromDistance(
        distanceMeters: Double,
        speedMps: Double = 1.4
    ): Int {
        val seconds = distanceMeters / speedMps
        return ceil(seconds / 60).toInt()
    }

    fun formatMinutes(koreanMinutes: Int): String {
        if (koreanMinutes < 1) return "1분 미만"
        val hours = koreanMinutes / 60
        val minutes = koreanMinutes % 60
        return when {
            hours == 0 -> "${minutes}분"
            minutes == 0 -> "${hours}시간"
            else -> "${hours}시간 ${minutes}분"
        }
    }

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        private const val ARG_PAGE_NAME = "arg_page_name"
    }
}