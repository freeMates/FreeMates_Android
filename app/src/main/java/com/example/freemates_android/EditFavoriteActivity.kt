package com.example.freemates_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.freemates_android.databinding.ActivityEditFavoriteBinding
import com.example.freemates_android.databinding.ActivityLoginBinding
import com.example.freemates_android.model.map.FavoriteList

class EditFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFavoriteBinding
    private lateinit var favoriteList: FavoriteList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        favoriteList = intent.getParcelableExtra("arg_favorite_detail") ?: return

        initUI()
        clickEvent()
        textChangeEvent()
    }


    private fun initUI(){
        Glide.with(this)
            .load(favoriteList.thumbnailUrl)
            .into(binding.ivFavoriteImageEditFavorite)
        binding.etFavoriteTitleEditFavorite.setText(favoriteList.title)
        binding.btnFavoriteVisibilityPublicEditFavorite.isSelected = true
        binding.etFavoriteDescriptionEditFavorite.setText(favoriteList.description)
    }

    private fun clickEvent(){
        binding.btnBackToMapEditFavorite.setOnClickListener {
            finish()
        }

        binding.btnFavoriteVisibilityPublicEditFavorite.setOnClickListener {
            selectVisibility("공개")
        }

        binding.btnFavoriteVisibilityPrivateEditFavorite.setOnClickListener {
            selectVisibility("비공개")
        }

        binding.btnCompleteEditEditFavorite.setOnClickListener {
            finish()
        }
    }

    private fun textChangeEvent(){
        binding.etFavoriteTitleEditFavorite.addTextChangedListener {
            updateCompleteButtonState()
        }

        binding.etFavoriteDescriptionEditFavorite.addTextChangedListener {
            updateCompleteButtonState()
        }
    }

    private fun selectVisibility(visibilityStatus: String) {
        when (visibilityStatus) {
            "공개" -> {
                binding.btnFavoriteVisibilityPublicEditFavorite.isSelected = true
                binding.btnFavoriteVisibilityPrivateEditFavorite.isSelected = false
            }
            "비공개" -> {
                binding.btnFavoriteVisibilityPublicEditFavorite.isSelected = false
                binding.btnFavoriteVisibilityPrivateEditFavorite.isSelected = true
            }
        }
    }

    private fun updateCompleteButtonState() {
        val isValid: Boolean = binding.etFavoriteTitleEditFavorite.length() > 0 && binding.etFavoriteDescriptionEditFavorite.length() > 0
        binding.btnCompleteEditEditFavorite.isSelected = isValid
        binding.btnCompleteEditEditFavorite.isClickable = isValid
    }
}