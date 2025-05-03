package com.example.freemates_android.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.SheetPlacePreviewBinding
import com.example.freemates_android.model.map.Place

private const val ARG_PARAM1 = "arg_category"
private const val ARG_PARAM2 = "arg_places"

class FavoriteListSheet : Fragment() {
    companion object {
        private const val ARG_PLACE = "arg_favoritelist"

        fun newInstance(place: Place): FavoriteListSheet {
            val fragment = FavoriteListSheet()
            val args = Bundle()
            args.putParcelable(ARG_PLACE, place)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var place: Place

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
        val binding = SheetPlacePreviewBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정

        Glide.with(this)
            .load(place.thumbnailUrl)
            .into(binding.ivPlaceImagePlacePreview)
        binding.tvPlaceTitlePlacePreview.text = place.name
        binding.ibtnLikePlacePreview.isSelected = place.isFavorite
        binding.tvDistancePlacePreview.text = place.distance
        binding.tvAddressPlacePreview.text = place.address

        return binding.root
    }
}