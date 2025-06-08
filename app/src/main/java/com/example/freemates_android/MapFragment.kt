package com.example.freemates_android

import android.Manifest
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.MyBookmarkListResponse
import com.example.freemates_android.api.dto.PlaceDto
import com.example.freemates_android.databinding.FragmentMapBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.AddFavorite
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.sheet.CategoryResultSheet
import com.example.freemates_android.sheet.FavoriteListSheet
import com.example.freemates_android.sheet.PlacePreviewSheet
import com.example.freemates_android.ui.adapter.category.CategoryLargeAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.Poi
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(R.layout.fragment_map) {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var currentLatLng: LatLng = LatLng.from(37.5506, 127.0742)
    val IMAGE_BASE_URL = "http://3.34.78.124:8087"
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var favoriteList: ArrayList<FavoriteList>

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private lateinit var viewModel: MapViewModel

    private val recommendList = listOf(
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            "",  R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            "",  R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
    )

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMapBinding.bind(view)

        initPersistentSheet()
        initCategoryRecycler()
        initViewModelAndCollector()
        initCurrentLocation()
        showMapView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Event Click : ", "MapFragment onResume 실행")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Event Click : ", "MapFragment onStart 실행")
    }

    private fun initPersistentSheet() {
        val sheetBinding = binding.persistentSheet                    // include 루트 id
        val sheetView = sheetBinding.root
        sheetBehavior = BottomSheetBehavior.from(sheetView).apply {
            state      = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)
            isHideable = false
            skipCollapsed = false
        }

        // ─ Navigation 높이 + 인셋 보정 ─ //
        val navView = requireActivity().findViewById<View>(R.id.bottom_navigation)
        navView.doOnLayout {
            val navH = navView.height
            val insetBottom = ViewCompat.getRootWindowInsets(sheetView)
                ?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

            sheetBehavior.expandedOffset = navH + insetBottom
        }
    }

    private fun initCategoryRecycler() {
        val categoryList = arrayListOf(
            CategoryItem(R.drawable.ic_cafe_large_on, R.drawable.ic_cafe_large_off, "카페", false),
            CategoryItem(R.drawable.ic_leisure_large_on,R.drawable.ic_leisure_large_off, "놀거리", false),
            CategoryItem(R.drawable.ic_walk_large_on, R.drawable.ic_walk_large_off, "산책", false),
            CategoryItem(R.drawable.ic_foods_large_on, R.drawable.ic_foods_large_off, "먹거리", false),
            CategoryItem(R.drawable.ic_hospital_large_on, R.drawable.ic_hospital_large_off, "병원", false),
            CategoryItem(R.drawable.ic_shopping_large_on, R.drawable.ic_shopping_large_off, "쇼핑", false),
        )

        val spacingDecoration = HorizontalSpacingDecoration(requireContext(), 8)
        val adapter = CategoryLargeAdapter(requireContext(), categoryList) { category ->
            viewModel.showCategoryResult(category, recommendList)
        }

        binding.rvPlaceCategoryMap.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(spacingDecoration)
            setHasFixedSize(true)
        }
    }

    private fun initViewModelAndCollector() {
        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]

//        viewModel.showFavoriteList(emp)

        lifecycleScope.launchWhenStarted {
            viewModel.sheetState.collect { state ->
                Log.d("Event Click : ", "상태 수집됨: $state")
                when (state) {
                    is MapViewModel.SheetState.Collapsed -> {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    is MapViewModel.SheetState.PlacePreview -> {
                        updateSheetContent(state)
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    is MapViewModel.SheetState.CategoryResult -> {
                        updateSheetContent(state)
                        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    is MapViewModel.SheetState.FavoriteList -> {
                        updateSheetContent(state)
                        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
//                    is MapViewModel.SheetState.FavoriteDetail -> {
//                        Log.d("Event Click : ", "FavoriteDetail 상태로 전환")
//                        updateSheetContent(state)
//                        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                    }
                    is MapViewModel.SheetState.Hidden -> {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    else -> { }
                }
            }
        }
    }

    private fun updateSheetContent(state: MapViewModel.SheetState) {
        val (fragment, tag) = when (state) {
            is MapViewModel.SheetState.PlacePreview ->
                PlacePreviewSheet.newInstance(state.place) to "PlacePreview"
            is MapViewModel.SheetState.CategoryResult ->
                CategoryResultSheet.newInstance(state.category, state.places) to "CategoryResult"
            is MapViewModel.SheetState.FavoriteList ->
                FavoriteListSheet.newInstance(state.favoritelist!!) to "FavoriteList"
//            is MapViewModel.SheetState.FavoriteDetail ->
//                FavoriteDetailSheet.newInstance(state.list) to "FavoriteDetail"
            else -> return
        }

        childFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.sheet_container, fragment, tag)
            .commit()
    }
    
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun initCurrentLocation() {
        LocationServices.getFusedLocationProviderClient(requireContext())
            .lastLocation
            .addOnSuccessListener { loc: Location? ->
                loc?.let { currentLatLng = LatLng.from(it.latitude, it.longitude) }
            }
    }

    // 즐겨찾기 값 마커로 찍기
    fun addMarkersToMap(place: Place, pinColor: Int) {
        Log.d("Map", "지도에 핀 추가중...")
        val layer = kakaoMap!!.labelManager!!.layer      // null 아님 확정
        val style = kakaoMap!!.labelManager!!
            .addLabelStyles(LabelStyles.from(LabelStyle.from(pinColor)))

        val options = LabelOptions.from(LatLng.from(place.latitude, place.longitude))
            .setStyles(style)          // 마커 아이콘
            .setClickable(true)        // 클릭 가능
//        layer?.removeAll()
        val label = layer?.addLabel(options)

        // 여기서 place 객체(혹은 id)를 바로 태깅
        label?.tag = place

    }

    private fun showMapView() {

        mapView = binding.mvMap

        // KakaoMapSDK 초기화!!
        KakaoMapSdk.init(requireContext(), KAKAO_MAP_KEY)

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(p0: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "onMapError")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaomap: KakaoMap) {
                // 정상적으로 인증이 완료되었을 때 호출
                kakaoMap = kakaomap

                fetchBookmarkList()

//                addMarkersToMap(places)

                kakaomap.setOnMapClickListener { kakaoMap, position, screenPoint, poi ->
                    val clickedLat = position.latitude
                    val clickedLng = position.longitude

                    Log.d("Map", "지도 클릭 위치 - 위도: $clickedLat, 경도: $clickedLng")

                    fetchPlaceInfo(clickedLng.toString(), clickedLat.toString())
//                    Toast.makeText(
//                        requireContext(),
//                        "클릭한 좌표: 위도 $clickedLat, 경도 $clickedLng",
//                        Toast.LENGTH_SHORT
//                    ).show()

                }

                kakaoMap!!.setOnLabelClickListener { _, _, clickedLabel ->
                    val place = clickedLabel.tag as? Place ?: return@setOnLabelClickListener
                    viewModel.showPlacePreview(place)
                }

//                kakaoMap!!.setOnMapClickListener { _, _, _, _ ->
//                    viewModel.showFavoriteList(favoriteList)
//                }
            }

            override fun getPosition(): LatLng {
                Log.d("Map", "lat : ${currentLatLng.latitude}")
                Log.d("Map", "long : ${currentLatLng.longitude}")
                return currentLatLng
            }
        })
    }

    private fun addMarkerToMap(place: Place) {
        val layer = kakaoMap!!.labelManager!!.layer      // null 아님 확정
        val style = kakaoMap!!.labelManager!!
            .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_skyblue_marker_png)))

        val options = LabelOptions.from(LatLng.from(place.latitude, place.longitude))
            .setStyles(style)          // 마커 아이콘
            .setClickable(true)        // 클릭 가능
        layer?.removeAll()
        val label = layer?.addLabel(options)

        // 여기서 place 객체(혹은 id)를 바로 태깅
        label?.tag = place
    }

    private fun fetchPlaceInfo(long: String, lat: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val refreshToken = requireContext().getRefreshToken()

            RetrofitClient.placeService.placeGeoCode(
                "Bearer $refreshToken",
                long,
                lat
            ).enqueue(object :
                Callback<List<PlaceDto>> {
                override fun onResponse(
                    call: Call<List<PlaceDto>>,
                    response: Response<List<PlaceDto>>
                ) {
                    Log.d("AddFavoritePlace", "response code :${response.code()}")
                    when (response.code()) {
                        200 -> {
                            val item = response.body()?.get(0)
                            val place: Place? = item?.let {
                                Place(
                                    it.placeId,
                                    item.imageUrl,
                                    item.placeName,
                                    false,
                                    item.categoryType,
                                    item.tags,
                                    item.distance,
                                    item.addressName,
                                    item.y.toDouble(),
                                    item.x.toDouble()
                                )
                            }
                            if (place != null) {
                                viewModel.showPlacePreview(place)
                                addMarkerToMap(place)
                            }
                        }

                        404 -> {
                            Toast.makeText(requireContext(), "존재하지 않는 장소입니다.", Toast.LENGTH_SHORT).show()
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
                                                    placeItem.distance
                                                )
                                            )

                                            if(userNickname == items.nickname){
                                                addMarkersToMap(
                                                    Place(
                                                       placeItem.placeId,
                                                        placeItem.imageUrl,
                                                        placeItem.placeName,
                                                        true,
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
                                                        placeItem.distance,
                                                        placeItem.addressName,
                                                        placeItem.y.toDouble(),
                                                        placeItem.x.toDouble()
                                                    ),
                                                    when (items.pinColor.uppercase()) {
                                                        "RED" -> R.drawable.ic_red_marker_image
                                                        "YELLOW" -> R.drawable.ic_yellow_marker_image
                                                        "GREEN" -> R.drawable.ic_green_marker_image
                                                        "BLUE" -> R.drawable.ic_blue_marker_image
                                                        "PURPLE" -> R.drawable.ic_purple_marker_image
                                                        else -> R.drawable.ic_pink_marker_image
                                                    }
                                                )
                                            }
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

                            // TODO !!
                            viewModel.showFavoriteList(favoriteList)
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
}