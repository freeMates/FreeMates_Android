package com.example.freemates_android

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.FragmentPlaceInfoBinding
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.google.android.flexbox.FlexboxLayout
import kotlin.math.ceil

class PlaceInfoFragment : Fragment(R.layout.fragment_place_info) {
    private lateinit var binding: FragmentPlaceInfoBinding
    private lateinit var placeInfo: RecommendItem
    private lateinit var flexboxLayout: FlexboxLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPlaceInfoBinding.bind(view)

        placeInfo = requireArguments().getParcelable<RecommendItem>("placeInfo")!!

        binding.btnBackToHomePlaceInfo.setOnClickListener {
            findNavController().navigate(R.id.action_placeInfoFragment_to_homeFragment)
        }

        initUI()

        binding.clLikeContainerPlaceInfo.setOnClickListener {
            if(binding.ivLikePlaceInfo.isSelected){
                binding.ivLikePlaceInfo.isSelected = false
                binding.tvLikePlaceInfo.isSelected = false
            } else{
                binding.ivLikePlaceInfo.isSelected = true
                binding.tvLikePlaceInfo.isSelected = true
            }
        }

        binding.clGotoMapContainerPlaceInfo.setOnClickListener {
            findNavController().navigate(R.id.action_placeInfoFragment_to_mapFragment)
        }
    }

    private fun initUI(){
        // 이미지
        val imageUrl = placeInfo.placeImage?.replaceFirst("http://", "https://")
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.ivPlaceImagePlaceInfo)
        // 제목
        binding.tvPlaceTitlePlaceInfo.text = placeInfo.placeTitle
        // 한줄평
        binding.tvPlaceIntroPlaceInfo.text = placeInfo.placeIntro
        if (binding.tvPlaceIntroPlaceInfo.text.isEmpty()) {
            binding.tvPlaceIntroPlaceInfo.visibility = View.GONE
        } else{
            binding.tvPlaceIntroPlaceInfo.visibility = View.VISIBLE
        }
        // 주소
        binding.tvPlaceAddressPlaceInfo.text = placeInfo.placeAddress
        // 소요시간
        val minutes = placeInfo.placeDuration?.let { minutesFromDistance(it.toDouble()) }
        binding.tvPlaceDurationPlaceInfo.text = minutes?.let { formatMinutes(it) }
        // 카테고리
        binding.tvPlaceCategoryPlaceInfo.text = placeInfo.placeCategoryTitle
        val drawable = placeInfo.placeCategoryImage
        if (drawable != null) {
            binding.tvPlaceCategoryPlaceInfo.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
        }
        // 필터
        val texts = placeInfo.filter
        if (texts != null) {
            for (text in texts) {
                flexboxLayout = binding.fblPlaceFilterPlaceInfo
                val itemView = layoutInflater.inflate(R.layout.item_filter, flexboxLayout, false)
                val tv = itemView.findViewById<TextView>(R.id.tvFilter_FilterItem)
                tv.text = text

                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.rightMargin = resources.getDimensionPixelSize(R.dimen._4dp)
                lp.bottomMargin = resources.getDimensionPixelSize(R.dimen._8dp)
                itemView.layoutParams = lp

                flexboxLayout.addView(itemView)
            }
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