package com.example.freemates_android.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetPlacePreviewBinding
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.filter.FilterAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
import kotlin.math.ceil

class PlacePreviewSheet : Fragment() {
    companion object {
        private const val ARG_PLACE = "arg_place"

        fun newInstance(place: Place): PlacePreviewSheet {
            val fragment = PlacePreviewSheet()
            val args = Bundle()
            args.putParcelable(ARG_PLACE, place)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var place: Place
    private lateinit var binding: SheetPlacePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            place = it.getParcelable(ARG_PLACE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SheetPlacePreviewBinding.inflate(inflater, container, false)

        initUI()
        clickEvent()

        return binding.root
    }

    private fun initUI(){
        Glide.with(this)
            .load(place.thumbnailUrl)
            .placeholder(R.drawable.ic_image_default)
            .error(R.drawable.ic_image_default)
            .fallback(R.drawable.ic_image_default)
            .into(binding.ivPlaceImagePlacePreview)
        binding.tvPlaceTitlePlacePreview.text = place.name
        binding.ibtnLikePlacePreview.isSelected = place.isFavorite
        val minutes = place.distance?.toDouble()?.let { minutesFromDistance(it) }
        binding.tvPlaceIntroPlacePreview.text = "도보 ${minutes?.let { formatMinutes(it) }}"
        binding.tvAddressPlacePreview.text = place.address

        val filterHorizontalSpacingDecoration = HorizontalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 2,              // 아이템 간 간격
        )
        val filterAdapter = FilterAdapter(requireContext(), place.tags)
        binding.rvFilterPlacePreview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilterPlacePreview.addItemDecoration(filterHorizontalSpacingDecoration)
        binding.rvFilterPlacePreview.adapter = filterAdapter
    }

    private fun clickEvent(){
        binding.ibtnLikePlacePreview.setOnClickListener {
            val addFavoritePlace = AddFavoritePlaceSheet()

            // Place 데이터를 BottomSheet에 전달
//            addFavoritePlace.setPlaceData(place)

            val bundle = bundleOf("placeId" to place.id)
            addFavoritePlace.arguments = bundle

            // BottomSheet 띄우기
            addFavoritePlace.show(childFragmentManager, addFavoritePlace.tag)
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