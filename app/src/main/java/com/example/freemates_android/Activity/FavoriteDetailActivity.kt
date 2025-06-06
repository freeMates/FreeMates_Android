package com.example.freemates_android.Activity

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ActivityFavoriteDetailBinding
import com.example.freemates_android.databinding.ActivityFindIdBinding
import com.example.freemates_android.model.map.FavoriteList
import com.example.freemates_android.ui.adapter.recommend.RecommendAdapter
import com.example.freemates_android.ui.decoration.VerticalSpacingDecoration

class FavoriteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteDetailBinding
    private lateinit var favoriteList: FavoriteList

    companion object {
        private const val ARG_FAVORITE_DETAIL = "arg_favorite_detail"
        private const val ARG_PAGE_NAME = "arg_page_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        favoriteList = intent.getParcelableExtra(ARG_FAVORITE_DETAIL) ?: return

        binding.btnBackToRecommendFavoriteDetail.setOnClickListener {
            finish()
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
            context = this,
            spacingDp = 12,
        )

        val userFavoritePlacesAdapter = RecommendAdapter(this, ArrayList(favoriteList.places))
        binding.rvFavoritePlacesFavoriteDetail.apply {
            adapter = userFavoritePlacesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(favoriteDetailVerticalSpacingDecoration)
            setHasFixedSize(true)
        }

        clickEvent()

    }


    private fun clickEvent(){
        binding.btnShareFavoriteDetail.setOnClickListener {
            showShareDialog()
        }

        binding.btnEditFavoriteDetail.setOnClickListener {
            val intent = Intent(this, EditFavoriteActivity::class.java).apply {
                putExtra(ARG_FAVORITE_DETAIL, favoriteList)
                putExtra(ARG_PAGE_NAME, "favoriteDetail")
            }
            startActivity(intent)
        }
    }

    private fun showShareDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null)
        val dialog = AlertDialog.Builder(this)
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
            Toast.makeText(this, "KakaoTalk is not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    // URL 클립보드에 복사
    private fun copyUrlToClipboard(url: String) {
        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = android.content.ClipData.newPlainText("URL", url)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "URL copied to clipboard.", Toast.LENGTH_SHORT).show()
    }
}