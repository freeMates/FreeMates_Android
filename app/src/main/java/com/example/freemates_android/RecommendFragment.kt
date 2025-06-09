package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.api.dto.CourseDto
import com.example.freemates_android.api.dto.CourseResponse
import com.example.freemates_android.api.dto.PageBookmarkResponse
import com.example.freemates_android.databinding.FragmentRecommendBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.course.CourseAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class RecommendFragment : Fragment(R.layout.fragment_recommend) {
    private lateinit var binding: FragmentRecommendBinding
    private var courseList = ArrayList<Course>()
    private var recommendList = ArrayList<RecommendItem>()
    private val favoriteList: ArrayList<FavoriteList> = ArrayList<FavoriteList>()
    private lateinit var loadingDialog: LoadingDialog
    private var bookmarkFlag = false
    private var recommendFlag = false
    private var courseFlag = false

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRecommendBinding.bind(view)

        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoading()

        recyclerviewInit()
        clickEvent()
    }

    private fun recyclerviewInit(){
        getCourseData()
        getBookmarkData()
        getRecommendData()
    }

    private fun clickEvent(){
        binding.tvPopularCourseDetailRecommend.setOnClickListener {
            val bundle = bundleOf("courseList" to courseList)
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment, bundle)
        }

        binding.ivAdsRecommend.setOnClickListener{
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment)
        }

        binding.fabAddCourseRecommend.setOnClickListener {
            val course = Course(
                "R.drawable.image2",
                "",
                "",
                false,
                0,
                "0",
                "",
                emptyList(),
                emptyList(),
                false
            )

            val bundle = bundleOf("courseInfo" to course)
            findNavController().navigate(R.id.action_recommendFragment_to_courseEditFragment, bundle)
        }
    }

    private fun getCourseData(){
        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
            val myName = requireContext().getNicknameInfo()
            val refreshToken = requireContext().getRefreshToken()
            Log.d("Home", "refreshToken : $refreshToken")
            RetrofitClient.courseService.getCourseList(
                "Bearer $refreshToken",
                "PUBLIC",
                0,
                10
            ).enqueue(object :
                Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    Log.d("Home", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.empty == false) {
                                for (items in response.body()!!.content) {
                                    val imageUrl = IMAGE_BASE_URL + items.imageUrl
                                    val placeList: ArrayList<RecommendItem> = ArrayList()
                                    var courseDuration: Int = 0
                                    val courseCategory: ArrayList<Category> = ArrayList()

                                    for(place in items.placeDtos){
                                        val tmpItem = RecommendItem(
                                            place.placeId,
                                            place.imageUrl,
                                            place.placeName,
                                            myName == items.nickname,
                                            place.likeCount,
                                            place.addressName,
                                            when(place.categoryType.uppercase()) {
                                                "CAFE" -> R.drawable.ic_cafe_small_on
                                                "FOOD" -> R.drawable.ic_foods_small_on
                                                "SHOPPING" -> R.drawable.ic_shopping_small_on
                                                "WALK" -> R.drawable.ic_walk_small_on
                                                "PLAY" -> R.drawable.ic_leisure_small_on
                                                "HOSPITAL" -> R.drawable.ic_hospital_small_on
                                                else -> R.drawable.ic_cafe_small_on
                                            },
                                            when(place.categoryType.uppercase()) {
                                                "CAFE" -> "카페"
                                                "FOOD" -> "먹거리"
                                                "SHOPPING" -> "쇼핑"
                                                "WALK" -> "산책"
                                                "PLAY" -> "놀거리"
                                                "HOSPITAL" -> "병원"
                                                else -> "카페"
                                            },
                                            place.tags,
                                            place.introText,
                                            place.distance
                                        )
                                        placeList.add(tmpItem)

//                                        tmpItem.placeCategoryImage?.let {
//                                            tmpItem.placeCategoryTitle?.let { it1 ->
//                                                Category(
//                                                    it, it1
//                                                )
//                                            }
//                                        }?.let { courseCategory.add(it) }

                                        tmpItem.placeCategoryImage?.let {
                                            tmpItem.placeCategoryTitle?.let { it1 ->
                                                Category(
                                                    it,
                                                    it1
                                                )
                                            }
                                        }?.let {
                                            courseCategory.add(
                                                it
                                            )
                                        }

                                        courseDuration += tmpItem.placeDuration?.toInt() ?: 0
                                    }

                                    Log.d("Recommend", "placeList : $placeList")
                                    Log.d("Recommend", "courseCategory : $courseCategory")

                                    val item = Course(
                                        imageUrl,
                                        items.title,
                                        items.nickname,
                                        myName == items.nickname,
                                        items.likeCount,
                                        when(courseDuration) {
                                            30  -> "30분 소요 코스"
                                            60  -> "1시간 소요 코스"
                                            90  -> "1시간 30분 소요 코스"
                                            120 -> "2시간 소요 코스"
                                            150 -> "2시간 30분 소요 코스"
                                            else -> "3시간 소요 코스"
                                        },
                                        items.description,
                                        placeList.toList(),
                                        courseCategory,
                                        true
                                    )
                                    courseList.add(item)
                                }


                            }
                            courseRecyclerviewInit()

                            courseFlag = true
                            dialogDismiss()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")

                            courseFlag = true
                            dialogDismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)

                    courseFlag = true
                    dialogDismiss()
                }
            })
        }
    }

    private fun courseRecyclerviewInit(){
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
//                                    if(!items.placeDto.isNullOrEmpty()) {

                                    Log.d("Recommend", "item : $items")
                                    items.placeDtos?.takeIf { it.isNotEmpty() }?.forEach { placeItem ->
                                        Log.d("Recommend", "items.placeDto is not null or empty")
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
//                                        }


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

                            bookmarkFlag = true
                            dialogDismiss()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")

                            bookmarkFlag = true
                            dialogDismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<PageBookmarkResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)

                    bookmarkFlag = true
                    dialogDismiss()
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

        binding.rvFavoriteListRecommend.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
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

                            recommendFlag = true
                            dialogDismiss()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")

                            recommendFlag = true
                            dialogDismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)

                    recommendFlag = true
                    dialogDismiss()
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
                findNavController().navigate(R.id.action_recommendFragment_to_placeInfoFragment, bundle)
            }
        })

        binding.rvPlaceListRecommend.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }

    private fun dialogDismiss(){
        if(recommendFlag && bookmarkFlag && courseFlag)
            loadingDialog.hideLoading()
    }
}