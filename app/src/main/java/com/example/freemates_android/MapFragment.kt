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

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private lateinit var viewModel: MapViewModel


//    val places = listOf(
//        Place(
//            id = "place_001",
//            thumbnailUrl = "https://example.com/image.jpg",
//            name = "세종대 학생회관",
//            isFavorite = false,
//            categories = listOf("카페", "디저트"),
//            tags = listOf("조용한", "맛있는 디저트"),
//            distance = "10분",
//            address = "서울특별시 중구 세종대로 110",
//            latitude = 37.5494,
//            longitude = 127.0750
//        ),
//        Place(
//            id = "place_002",
//            thumbnailUrl = "https://picsum.photos/seed/picsum/200/300",
//            name = "어린이대공원역",
//            isFavorite = false,
//            categories = listOf("카페", "디저트"),
//            tags = listOf("조용한", "맛있는 디저트"),
//            distance = "10분",
//            address = "서울특별시 중구 세종대로 110",
//            latitude = 37.5479,
//            longitude = 127.0746
//        ),
//        Place(
//            id = "place_003",
//            thumbnailUrl = "https://picsum.photos/seed/picsum/200/300",
//            name = "행복한 카페",
//            isFavorite = false,
//            categories = listOf("카페", "디저트"),
//            tags = listOf("조용한", "맛있는 디저트"),
//            distance = "10분",
//            address = "서울특별시 중구 세종대로 110",
//            latitude = 37.5462,
//            longitude = 127.0733
//        )
//    )

    private val recommendList = listOf(
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf(("콘센트가 있어요"), ("조용해요"), ("좌석이 많아요")), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
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

        viewModel.showFavoriteList()

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

    fun updateSheetContent(state: MapViewModel.SheetState) {
        val (fragment, tag) = when (state) {
            is MapViewModel.SheetState.PlacePreview ->
                PlacePreviewSheet.newInstance(state.place) to "PlacePreview"
            is MapViewModel.SheetState.CategoryResult ->
                CategoryResultSheet.newInstance(state.category, state.places) to "CategoryResult"
            is MapViewModel.SheetState.FavoriteList ->
                FavoriteListSheet.newInstance(state.favoritelist) to "FavoriteList"
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

//                addMarkersToMap(places)

                kakaomap.setOnMapClickListener { kakaoMap, position, screenPoint, poi ->
                    val clickedLat = position.latitude
                    val clickedLng = position.longitude

                    Log.d("Map", "지도 클릭 위치 - 위도: $clickedLat, 경도: $clickedLng")

                    // TODO 클릭 시 위치의 장소 띄우기
                    fetchPlaceInfo(clickedLng.toString(), clickedLat.toString())
//                    Toast.makeText(
//                        requireContext(),
//                        "클릭한 좌표: 위도 $clickedLat, 경도 $clickedLng",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    // 예: 클릭한 좌표를 ViewModel로 전달하고 싶다면
                    // viewModel.handleMapClick(clickedLat, clickedLng)
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

    private fun addMarkersToMap(place: Place) {
        val layer = kakaoMap!!.labelManager!!.layer      // null 아님 확정
        val style = kakaoMap!!.labelManager!!
            .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_skyblue_marker_png)))

        val options = LabelOptions.from(LatLng.from(place.latitude, place.longitude))
            .setStyles(style)          // 마커 아이콘
            .setClickable(true)        // 클릭 가능
        layer?.removeAll()
        val label = layer?.addLabel(options)

        // 여기서 place 객체(혹은 id)를 바로 태깅
        label?.tag = place            // ★ 핵심 ★

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
                                addMarkersToMap(place)
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
}