package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.EditFavoriteActivity
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.api.dto.CourseDto
import com.example.freemates_android.api.dto.CourseResponse
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

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRecommendBinding.bind(view)

        recyclerviewInit()
        clickEvent()
    }

    private fun recyclerviewInit(){
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기1", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기2", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기3", "파인애플농부애옹", true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))
//        courseList.add(Course(R.drawable.image2, "맛집 탐방하기4", "파인애플농부애옹",true, 13, "2시간 소요 코스", "광진구 구석구석을 누비며 만나는 진짜 맛의 세계. 입과 마음이 모두 행복해지는 맛집 탐방 코스!", listOf(Category(R.drawable.ic_category_cafe, "카페"), Category(R.drawable.ic_category_sports, "스포츠")), true))

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

        val favoriteListInfo = FavoriteList(R.drawable.ic_yellow_marker, "브랫서울", "R.drawable.image2",
            "서울 광진구 광나루로 410 1층 101호", emptyList(), false)

        val favoriteAdapter = FavoriteAdapter(requireContext(), favoriteList)
        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(item: FavoriteItem) {
                val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                    putExtra(ARG_FAVORITE_DETAIL, favoriteListInfo)
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

        getCourseData()
        getRecommendData()
    }

    private fun clickEvent(){
        binding.tvPopularCourseDetailRecommend.setOnClickListener {
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment)
        }

        binding.ivAdsRecommend.setOnClickListener{
            findNavController().navigate(R.id.action_recommendFragment_to_durationCourseFragment)
        }
    }

    private fun getCourseData(){
        viewLifecycleOwner.lifecycleScope.launch {
            // ① suspend 함수 안전 호출
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
                                    var placeList: ArrayList<RecommendItem> = ArrayList()
                                    var courseDuration: Int = 0
                                    var courseCategory: ArrayList<Category> = ArrayList()
                                    for(place in items.coursePlaceDtos){
                                        // TODO duration이 없음(PLaceDTO에.
                                        val tmpItem = RecommendItem(
                                            place.imageUrl,
                                            place.placeName,
                                        false,
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
                                            formatMinutes(minutesFromDistance(place.distance.toDouble()))
                                        )

                                        placeList.add(tmpItem)
                                        tmpItem.placeCategoryImage?.let {
                                            tmpItem.placeCategoryTitle?.let { it1 ->
                                                Category(
                                                    it, it1
                                                )
                                            }
                                        }?.let { courseCategory.add(it) }
                                        courseDuration += tmpItem.placeDuration?.toInt() ?: 0
                                    }

                                    // TODO API 수정되면 items.distance 넣기
                                    val item = Course(
                                        items.imageUrl,
                                        items.title,
                                        items.nickname,
                                        false,
                                        743,
                                        courseDuration.toString(),
                                        items.description,
                                        placeList.toList(),
                                        courseCategory,
                                        true
                                    )
                                    courseList.add(item)
                                }


                            }
                            courseRecyclerviewInit()
                        }

                        else -> {
                            val errorCode = response.errorBody()?.string()
                            Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                    Log.d("ProfileSetup", value)
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
                findNavController().navigate(R.id.action_recommendFragment_to_placeInfoFragment)
            }
        })

        binding.rvPlaceListRecommend.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }

    fun minutesFromDistance(distanceMeters: Double,
                            speedMps: Double = 1.4): Int {
        val seconds = distanceMeters / speedMps
        return ceil(seconds / 60).toInt()
    }

    fun formatMinutes(koreanMinutes: Int): String {
        if (koreanMinutes < 1) return "1분 미만"
        val hours = koreanMinutes / 60
        val minutes = koreanMinutes % 60
        return when {
            hours == 0        -> "${minutes}분"
            minutes == 0      -> "${hours}시간"
            else              -> "${hours}시간 ${minutes}분"
        }
    }
}