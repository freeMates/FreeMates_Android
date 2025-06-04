package com.example.freemates_android.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetCategoryResultBinding
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class CategoryResultSheet : Fragment() {
    companion object {
        private const val ARG_CATEGORY = "arg_category"
        private const val ARG_PLACES = "arg_places"

        fun newInstance(category: String, places: List<RecommendItem>): CategoryResultSheet {
            val fragment = CategoryResultSheet()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            args.putParcelableArrayList(ARG_PLACES, ArrayList(places))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var category: String
    private lateinit var places: ArrayList<RecommendItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY)!!
            places = it.getParcelableArrayList(ARG_PLACES)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = SheetCategoryResultBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정

        val recommendVerticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 8,
        )

        val recommendAdapter = RecommendAdapter(requireContext(), places)
        recommendAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
            override fun onItemClick(item: RecommendItem) {
                findNavController().navigate(R.id.action_homeFragment_to_placeInfoFragment)
            }
        })

        binding.tvPlaceCategoryPromptCategoryResult.text = category
        binding.rvPlaceListCategoryResult.apply {
            adapter = recommendAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(recommendVerticalSpacingDecoration)
            setHasFixedSize(true)
        }

        return binding.root
    }
}