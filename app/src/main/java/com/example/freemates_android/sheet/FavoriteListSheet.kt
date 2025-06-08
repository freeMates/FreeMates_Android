package com.example.freemates_android.sheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.MapFragment
import com.example.freemates_android.MapViewModel
import com.example.freemates_android.R
import com.example.freemates_android.RecommendFragment
import com.example.freemates_android.RecommendFragment.Companion
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.MyBookmarkListResponse
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.AddFavorite
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class FavoriteListSheet : Fragment() {

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        private const val ARG_FAVORITE_SOURCE = "arg_favorite_source"

        fun newInstance(favoriteList: List<FavoriteList>?): FavoriteListSheet {
            val fragment = FavoriteListSheet()
            val args = Bundle()
            args.putParcelable(ARG_FAVORITE_DETAIL, null)
            fragment.arguments = args
            return fragment
        }
    }

    private val recommendList = listOf(
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
    )

    private lateinit var binding: SheetFavoriteListBinding
    private lateinit var favoriteList: ArrayList<FavoriteList>
    private lateinit var viewModel: MapViewModel

    val IMAGE_BASE_URL = "http://3.34.78.124:8087"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SheetFavoriteListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]

        lifecycleScope.launch {
            val userNickname: String = requireContext().getNicknameInfo()
            binding.tvFavoriteListPromptFavoriteList.text = "${userNickname}님의 목록"
        }

        fetchBookmarkList()

        return binding.root
    }

    private fun fetchBookmarkList(){
        Log.d("FavoriteList", "데이터 불러오기 시작")
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
                    Log.d("FavoriteList", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            val tempList = ArrayList<FavoriteList>()
                            response.body()?.forEach { items ->
                                val pinColorRes: Int = when (items.pinColor.uppercase()) {
                                    "RED" -> R.drawable.ic_red_marker
                                    "YELLOW" -> R.drawable.ic_yellow_marker
                                    "GREEN" -> R.drawable.ic_green_marker
                                    "BLUE" -> R.drawable.ic_blue_marker
                                    "PURPLE" -> R.drawable.ic_purple_marker
//                                    "PINK" -> R.drawable.ic_pink_marker
                                    else -> R.drawable.ic_pink_marker
                                }
                                val visibilityStatus: Boolean =
                                    items.visibility.uppercase() == "PUBLIC"

                                val imageUrl = IMAGE_BASE_URL + items.imageUrl

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


                                val item = items.bookmarkId?.let {
                                    FavoriteList(
                                        pinColorRes,
                                        items.title,
                                        imageUrl,
                                        items.description,
                                        placeList,
                                        visibilityStatus,
                                        items.nickname,
                                        it
                                    )
                                }

                                if (items.nickname == userNickname) {
                                    if (item != null) {
                                        tempList.add(item)


                                    }
                                }
                            }
                            favoriteList = tempList

                            setupRecyclerView()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("FavoriteList", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }
                }

                override fun onFailure(call: Call<List<MyBookmarkListResponse>>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("FavoriteList", value)
                }
            })
        }
    }

    private fun setupRecyclerView(){
        val favoriteListVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 12,
        )

        val userFavoriteAdapter = UserFavoriteAdapter(requireContext(), favoriteList){ selectedFavorite ->
            val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                putExtra(ARG_FAVORITE_DETAIL, selectedFavorite)
            }
            startActivity(intent)
//            viewModel.showFavoriteDetail(selectedFavorite) // 클릭된 아이템으로 상태 전환
            Log.d("FavoriteList", "Event Click : evnet1231")
        }

        binding.rvUserFavoriteFavoriteList.apply {
            adapter = userFavoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(favoriteListVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
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
}