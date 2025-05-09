package com.example.freemates_android.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetFavoriteDetailBinding
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class FavoriteDetailSheet : Fragment() {
    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"

        fun newInstance(favoriteList: FavoriteList): FavoriteDetailSheet {
            val fragment = FavoriteDetailSheet()
            val args = Bundle()
            args.putParcelable(ARG_FAVORITE_DETAIL, favoriteList)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var favoriteList: FavoriteList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favoriteList = it.getParcelable(ARG_FAVORITE_DETAIL)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = SheetFavoriteDetailBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정

        Log.d("Event Click : ", "fragment changed")
        Glide.with(this)
            .load(favoriteList.thumbnailUrl)
            .into(binding.ivFavoriteImageFavoriteDetail)

        Glide.with(this)
            .load(favoriteList.markerColor)
            .into(binding.ivFavoriteMarkerFavoriteDetail)

        binding.tvFavoriteTitleFavoriteDetail.text = favoriteList.title
        binding.tvFavoriteDescriptionFavoriteDetail.text = favoriteList.description
        binding.tvPlaceCntFavoriteDetail.text = "${favoriteList.places.size} 장소"

        val favoriteDetailVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 12,
        )
        val userFavoritePlacesAdapter = RecommendAdapter(requireContext(), ArrayList(favoriteList.places))
        binding.rvFavoritePlacesFavoriteDetail.apply {
            adapter = userFavoritePlacesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(favoriteDetailVerticalSpacingDecoration)
            setHasFixedSize(true)
        }


        return binding.root
    }
}