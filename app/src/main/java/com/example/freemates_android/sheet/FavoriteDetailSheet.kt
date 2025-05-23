package com.example.freemates_android.sheet

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.example.freemates_android.EditFavoriteActivity
import com.example.freemates_android.FindIdActivity
import com.example.freemates_android.R
import com.example.freemates_android.databinding.SheetFavoriteDetailBinding
import com.example.freemates_android.databinding.SheetFavoriteListBinding
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.favorite.UserFavoriteAdapter
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.findNavController

class FavoriteDetailSheet : Fragment() {
    companion object {
        const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        const val ARG_FAVORITE_SOURCE = "arg_favorite_source"

        fun newInstance(favoriteList: FavoriteList): FavoriteDetailSheet {
            val fragment = FavoriteDetailSheet()
            val args = Bundle()
            args.putParcelable(ARG_FAVORITE_DETAIL, favoriteList)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var favoriteList: FavoriteList
    private lateinit var binding: SheetFavoriteDetailBinding
    private var sourceTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favoriteList = it.getParcelable(ARG_FAVORITE_DETAIL)!!
            sourceTag    = it.getString(ARG_FAVORITE_SOURCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SheetFavoriteDetailBinding.inflate(inflater, container, false)
        // UI 초기화 및 이벤트 설정
        Log.d("소스태그 : ",sourceTag.toString())
        if(sourceTag == "recommend"){
            binding.clTopContainerFavoriteDetail.visibility = View.VISIBLE
        } else {
            binding.clTopContainerFavoriteDetail.visibility = View.GONE
        }

        binding.btnBackToRecommendFavoriteDetail.setOnClickListener {
            findNavController().navigate(R.id.action_sheetFavoriteDetailFragment_to_recommendFragment)
        }

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

        clickEvent()

        return binding.root
    }

    private fun clickEvent(){
        binding.btnShareFavoriteDetail.setOnClickListener {
            showShareDialog()
        }

        binding.btnEditFavoriteDetail.setOnClickListener {
            val intent = Intent(requireContext(), EditFavoriteActivity::class.java).apply {
                putExtra(ARG_FAVORITE_DETAIL, favoriteList)
            }
            startActivity(intent)
        }
    }

    private fun showShareDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_share, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val btnKakao = dialogView.findViewById<ImageView>(R.id.ivShareKakao_DialogShare)
        val btnUrl = dialogView.findViewById<ImageView>(R.id.ivShareUrl_DialogShare)
        val btnCancel = dialogView.findViewById<TextView>(R.id.tvCancel_DialogShare)

        // 카카오톡 공유 클릭
        btnKakao.setOnClickListener {
            shareToKakao()
            dialog.dismiss()
        }

        // URL 복사 클릭
        btnUrl.setOnClickListener {
            copyUrlToClipboard("https://your-url.com")
            dialog.dismiss()
        }

        // 취소 버튼
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // 카카오톡 공유 함수
    private fun shareToKakao() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.setPackage("com.kakao.talk")
            intent.putExtra(Intent.EXTRA_TEXT, "Check this out! https://your-url.com")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "KakaoTalk is not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    // URL 클립보드에 복사
    private fun copyUrlToClipboard(url: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = android.content.ClipData.newPlainText("URL", url)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "URL copied to clipboard.", Toast.LENGTH_SHORT).show()
    }
}