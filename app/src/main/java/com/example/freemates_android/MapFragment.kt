package com.example.freemates_android

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentMapBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapFragment : Fragment(R.layout.fragment_map) {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var currentLatLng: LatLng = LatLng.from(37.5506, 127.0742)

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private lateinit var viewModel: MapViewModel
    private var bottomSheet: MapBottomSheetDialogFragment? = null


    val places = listOf(
        Place(
            id = "place_001",
            thumbnailUrl = "https://example.com/image.jpg",
            name = "세종대 학생회관",
            isFavorite = false,
            categories = listOf("카페", "디저트"),
            tags = listOf("조용한", "맛있는 디저트"),
            distance = "10분",
            address = "서울특별시 중구 세종대로 110",
            latitude = 37.5494,
            longitude = 127.0750
        ),
        Place(
            id = "place_002",
            thumbnailUrl = "https://picsum.photos/seed/picsum/200/300",
            name = "어린이대공원역",
            isFavorite = false,
            categories = listOf("카페", "디저트"),
            tags = listOf("조용한", "맛있는 디저트"),
            distance = "10분",
            address = "서울특별시 중구 세종대로 110",
            latitude = 37.5479,
            longitude = 127.0746
        ),
        Place(
            id = "place_003",
            thumbnailUrl = "https://picsum.photos/seed/picsum/200/300",
            name = "행복한 카페",
            isFavorite = false,
            categories = listOf("카페", "디저트"),
            tags = listOf("조용한", "맛있는 디저트"),
            distance = "10분",
            address = "서울특별시 중구 세종대로 110",
            latitude = 37.5462,
            longitude = 127.0733
        )
    )

    private val recommendList = listOf(
        RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
            listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요"))),
        RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
            listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요"))),
        RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
            listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요"))),
        RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
            listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요"))),
        RecommendItem(R.drawable.image2, "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small, "카페",
            listOf(FilterItem("콘센트가 있어요"), FilterItem("조용해요"), FilterItem("좌석이 많아요"))),
    )

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMapBinding.bind(view)

        initCategoryRecycler()
        initViewModelAndCollector()
        initCurrentLocation()
        showMapView()
    }

    private fun initCategoryRecycler() {
        val categoryList = arrayListOf(
            CategoryItem(R.drawable.ic_cafe_off, "카페"),
            CategoryItem(R.drawable.ic_walk_off, "산책"),
            CategoryItem(R.drawable.ic_activity_off, "놀거리"),
            CategoryItem(R.drawable.ic_shopping_off, "쇼핑"),
            CategoryItem(R.drawable.ic_foods_off, "먹거리"),
            CategoryItem(R.drawable.ic_sports_off, "스포츠")
        )

        val spacingDecoration = HorizontalSpacingDecoration(requireContext(), 8)
        val adapter = CategorySmallAdapter(requireContext(), categoryList) { category ->
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
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            viewModel.sheetState.collect { state ->
                when (state) {
                    is MapViewModel.SheetState.Collapsed -> {
                        ensureBottomSheet { it.collapse() }
                    }

                    is MapViewModel.SheetState.PlacePreview,
                    is MapViewModel.SheetState.CategoryResult -> {
                        ensureBottomSheet { it.updateContent(state) }
                    }

                    is MapViewModel.SheetState.Hidden -> {
                        bottomSheet?.dismissAllowingStateLoss()
                        bottomSheet = null
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun ensureBottomSheet(onReady: (MapBottomSheetDialogFragment) -> Unit) {
        if (bottomSheet == null || !bottomSheet!!.isAdded) {
            bottomSheet = MapBottomSheetDialogFragment()
            // show( ) 는 메인 스레드 다음 프레임에서 실행 → attach 이후 onReady 보장
            view?.post {
                bottomSheet?.show(parentFragmentManager, "MapBottomSheet")
            }
        }
        bottomSheet!!.doWhenSheetReady(onReady)
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

                addMarkersToMap(places)

                kakaoMap!!.setOnLabelClickListener { _, _, clickedLabel ->
                    val place = clickedLabel.tag as? Place ?: return@setOnLabelClickListener
                    viewModel.showPlacePreview(place)
                }
            }

            override fun getPosition(): LatLng {
                return currentLatLng
            }
        })
    }

    private fun addMarkersToMap(places: List<Place>) {
        val layer = kakaoMap!!.labelManager!!.layer      // null 아님 확정
        places.forEach { place ->
            val style = kakaoMap!!.labelManager!!
                .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_skyblue_marker)))

            val options = LabelOptions.from(LatLng.from(place.latitude, place.longitude))
                .setStyles(style)          // 마커 아이콘
                .setClickable(true)        // 클릭 가능
            val label = layer?.addLabel(options)

            // 여기서 place 객체(혹은 id)를 바로 태깅
            label?.tag = place            // ★ 핵심 ★
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}