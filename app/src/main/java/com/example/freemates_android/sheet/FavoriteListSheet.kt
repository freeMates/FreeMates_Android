package com.example.freemates_android.sheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.Activity.FavoriteDetailActivity
import com.example.freemates_android.MapViewModel
import com.example.freemates_android.R
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import kotlinx.coroutines.launch

class FavoriteListSheet : Fragment() {
    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        private const val ARG_FAVORITE_SOURCE = "arg_favorite_source"

        fun newInstance(favoriteList: List<FavoriteList>): FavoriteListSheet {
            val fragment = FavoriteListSheet()
            val args = Bundle()
            args.putParcelableArrayList(ARG_FAVORITE_DETAIL, ArrayList(favoriteList))
            fragment.arguments = args
            return fragment
        }
    }

    private val recommendList = listOf(
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
        RecommendItem(
            "", R.drawable.image2.toString(), "브랫서울", true, 1345,
            "서울 광진구 광나루로 410 1층 101호", R.drawable.ic_cafe_small_on, "카페",
            listOf("콘센트가 있어요", "조용해요", "좌석이 많아요"), "", ""),
    )
    private lateinit var binding: SheetFavoriteListBinding
    private lateinit var viewModel: MapViewModel
//    private var favoriteListFromArgs: ArrayList<FavoriteList>? = null
    private lateinit var favoriteList: ArrayList<FavoriteList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favoriteList = it.getParcelableArrayList(ARG_FAVORITE_DETAIL)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SheetFavoriteListBinding.inflate(inflater, container, false)

        // 여기서 RecyclerView adapter에 넘기면 됨
        val userFavoriteAdapter = UserFavoriteAdapter(requireContext(), favoriteList) { selectedFavorite ->
                    val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                        putExtra(ARG_FAVORITE_DETAIL, selectedFavorite)
                    }
                    startActivity(intent)
                }

        binding.rvUserFavoriteFavoriteList.adapter = userFavoriteAdapter

        viewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]

        lifecycleScope.launch {
            val userNickname: String = requireContext().getNicknameInfo()
            binding.tvFavoriteListPromptFavoriteList.text = "${userNickname}님의 목록"
        }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val favoriteListVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 12,
        )

        val userFavoriteAdapter = UserFavoriteAdapter(requireContext(), favoriteList) { selectedFavorite ->
            val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                    putExtra(ARG_FAVORITE_DETAIL, selectedFavorite)
            }
            startActivity(intent)
            //            viewModel.showFavoriteDetail(selectedFavorite) // 클릭된 아이템으로 상태 전환
            Log.d("FavoriteList", "Event Click : evnet1231")
        }


        binding.rvUserFavoriteFavoriteList.apply {
            adapter = userFavoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(favoriteListVerticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}