package com.example.freemates_android.sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.MapFragment
import com.example.freemates_android.MapViewModel
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

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
    private lateinit var viewModel: MapViewModel

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

        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]

        val favoriteListVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 12,
        )

        val userFavoriteAdapter = UserFavoriteAdapter(requireContext(), favoriteList){ selectedFavorite ->
            viewModel.showFavoriteDetail(selectedFavorite) // 클릭된 아이템으로 상태 전환
            Log.d("Event Click : ", "evnet1231")
        }

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