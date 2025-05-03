package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.databinding.FragmentPlaceInfoBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.FavoriteItem
import com.example.freemates_android.ui.adapter.category.CategorySmallAdapter
import com.example.freemates_android.ui.adapter.favorite.FavoriteAdapter
import com.example.freemates_android.ui.decoration.HorizontalSpacingDecoration
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

        val categoryList = ArrayList<CategoryItem>()
        categoryList.add(CategoryItem(R.drawable.ic_cafe_on, "카페"))

        val favoriteHorizontalSpacingDecoration = HorizontalSpacingDecoration(
            context = requireContext(), // or `this` in Activity
            spacingDp = 8,              // 아이템 간 간격
        )

        val categorySmallAdapter = CategorySmallAdapter(requireContext(), categoryList){

        }

        binding.rvPlaceCategoryPlaceInfo.apply {
            adapter = categorySmallAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(favoriteHorizontalSpacingDecoration)
            setHasFixedSize(true)
        }

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