package com.example.freemates_android

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.SheetAddFavoritePlaceBinding
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.AddFavorite
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.favorite.AddFavoriteAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.graphics.toColorInt
import com.example.freemates_android.databinding.FragmentMapBinding

class AddFavoritePlaceSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SheetAddFavoritePlaceBinding

    private val recommendList = listOf(
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
    )

    private val favoriteList = listOf(
        AddFavorite(R.drawable.ic_red_marker, "절실한 카공이 필요할 때", R.drawable.image1,
            recommendList, true),
        AddFavorite(R.drawable.ic_yellow_marker, "먹짱의 먹거리 모음집ㅇ으아아ㅏ아아앙아아아ㅏ아앙아아앙아아ㅏ아ㅏ아", R.drawable.image2,
            recommendList, false),
        AddFavorite(R.drawable.ic_darkblue_marker, "먹짱의 먹거리 모음집ㅇ으아아ㅏ아아앙아아아ㅏ아앙아아앙아아ㅏ아ㅏ아", R.drawable.image3,
            recommendList, false),
    )

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
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = AddFavoriteAdapter(requireContext(), favoriteList) {
            // 헤더 클릭 시 (즐겨찾기 목록 추가 클릭)
            showAddFavoriteDialog()
        }
        binding.rvFavoriteListAddFavoritePlace.adapter = adapter
        binding.rvFavoriteListAddFavoritePlace.layoutManager = LinearLayoutManager(context)

        adapter.setOnItemClickListener {
            HeartOverlayUtil.showHeartOverlay(requireContext())
        }
    }

    private fun showAddFavoriteDialog() {
        // 여기에 즐겨찾기 추가 Dialog 구현
        Toast.makeText(requireContext(), "즐겨찾기 목록 추가 클릭됨", Toast.LENGTH_SHORT).show()
    }

//    fun setPlaceList(places: List<Place>) {
//        placeList.clear()
//        placeList.addAll(places)
//    }
}