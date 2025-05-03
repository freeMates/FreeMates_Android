package com.example.freemates_android.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetCategoryResultBinding
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.databinding.SheetPlacePreviewBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.model.map.Place
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

private const val ARG_PARAM1 = "arg_category"
private const val ARG_PARAM2 = "arg_places"

class FavoriteListSheet : Fragment() {
    companion object {
        private const val ARG_FAVORITE_LIST = "arg_favorite_list"

        fun newInstance(favoriteList: List<FavoriteList>): FavoriteListSheet {
            val fragment = FavoriteListSheet()
            val args = Bundle()
            args.putParcelableArrayList(ARG_FAVORITE_LIST, ArrayList(favoriteList))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var favoriteList: ArrayList<FavoriteList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favoriteList = it.getParcelableArrayList(ARG_FAVORITE_LIST)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = SheetFavoriteListBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정

        val favoriteListVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 12,
        )

        val userFavoriteAdapter = UserFavoriteAdapter(requireContext(), favoriteList)

        binding.tvFavoriteListPromptFavoriteList.text = "파인애플농부애옹님의 목록"
        binding.rvUserFavoriteFavoriteList.apply {
            adapter = userFavoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(favoriteListVerticalSpacingDecoration)
            setHasFixedSize(true)
        }

        return binding.root
    }
}