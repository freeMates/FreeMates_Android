package com.example.freemates_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemates_android.databinding.FragmentHomeBinding
import com.example.freemates_android.databinding.FragmentSearchBinding
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.SearchItem
import com.example.freemates_android.ui.adapter.gridview.category.CategoryAdapter
import com.example.freemates_android.ui.adapter.search.SearchAdapter
import com.example.freemates_android.ui.adapter.search.SearchViewHolder
import com.example.freemates_android.ui.decoration.GridSpacingDecoration
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSearchBinding.bind(view)

        val searchList = ArrayList<SearchItem>()
        searchList.add(SearchItem("공강시간 때 친구 없을 때 할 것 추천"))
        searchList.add(SearchItem("학교 주변 카페 추천"))
        searchList.add(SearchItem("전시회"))
        searchList.add(SearchItem("학교 근처 예쁜 포토존 있는 곳"))
        searchList.add(SearchItem("만화카페"))
        searchList.add(SearchItem("PC방"))
        searchList.add(SearchItem("공강 시간에 친구들이랑 가기 좋은 맛집"))


        val verticalSpacingDecoration = VerticalSpacingDecoration(
            context = requireContext(),
            spacingDp = 8
        )

        binding.rvRecentSearchTitleSearch.apply {
            adapter = this@SearchFragment.context?.let { SearchAdapter(it, searchList) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(verticalSpacingDecoration)
            setHasFixedSize(true)
        }
    }
}