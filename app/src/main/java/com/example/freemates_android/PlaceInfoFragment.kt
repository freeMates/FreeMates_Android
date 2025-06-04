package com.example.freemates_android

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.freemates_android.databinding.FragmentPlaceInfoBinding
import com.google.android.flexbox.FlexboxLayout

class PlaceInfoFragment : Fragment(R.layout.fragment_place_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentPlaceInfoBinding.bind(view)
        lateinit var flexboxLayout: FlexboxLayout

        binding.btnBackToHomePlaceInfo.setOnClickListener {
            findNavController().navigate(R.id.action_placeInfoFragment_to_homeFragment)
        }

        binding.clLikeContainerPlaceInfo.setOnClickListener {
            if(binding.ivLikePlaceInfo.isSelected){
                binding.ivLikePlaceInfo.isSelected = false
                binding.tvLikePlaceInfo.isSelected = false
            } else{
                binding.ivLikePlaceInfo.isSelected = true
                binding.tvLikePlaceInfo.isSelected = true
            }
        }

        binding.tvPlaceIntroPlaceInfo.text = "한 잔의 커피를 담습니다."

        binding.tvPlaceCategoryPlaceInfo.text = "먹거리"
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_foods_small_on)
        binding.tvPlaceCategoryPlaceInfo.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        val texts = listOf("다같이 즐길 수 있어요", "분위기가 좋아요", "조용해요", "와이파이 빵빵해요", "편하게 오래 앉을 수 있어요", "콘센트가 있어요", "혼자서도 좋아요", "화장실이 깨끗해요")

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