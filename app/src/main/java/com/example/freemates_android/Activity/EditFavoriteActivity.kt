package com.example.freemates_android.Activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.databinding.ActivityEditFavoriteBinding
import com.example.freemates_android.model.map.FavoriteList

class EditFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFavoriteBinding
    private lateinit var favoriteList: FavoriteList
    private lateinit var markerContainers: List<FrameLayout>

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
        binding.btnCompleteEditEditFavorite.isSelected = false
        binding.btnCompleteEditEditFavorite.isClickable = false

        Glide.with(this)
            .load(favoriteList.thumbnailUrl)
            .into(binding.ivFavoriteImageEditFavorite)
        binding.etFavoriteTitleEditFavorite.setText(favoriteList.title)
        binding.btnFavoriteVisibilityPublicEditFavorite.isSelected = true
        binding.etFavoriteDescriptionEditFavorite.setText(favoriteList.description)

        markerContainers = listOf(
            findViewById(R.id.flRedMarkerContainer_EditFavorite),
            findViewById(R.id.flYellowMarkerContainer_EditFavorite),
            findViewById(R.id.flGreenMarkerContainer_EditFavorite),
            findViewById(R.id.flDarkBlueMarkerContainer_EditFavorite),
            findViewById(R.id.flPurpleMarkerContainer_EditFavorite),
            findViewById(R.id.flPinkMarkerContainer_EditFavorite),
        )

        if (favoriteList.markerColor == R.drawable.ic_red_marker)
            selectMarker(0)
        else if(favoriteList.markerColor == R.drawable.ic_yellow_marker)
            selectMarker(1)
        else if(favoriteList.markerColor == R.drawable.ic_green_marker)
            selectMarker(2)
        else if(favoriteList.markerColor == R.drawable.ic_darkblue_marker)
            selectMarker(3)
        else if(favoriteList.markerColor == R.drawable.ic_purple_marker)
            selectMarker(4)
        else if(favoriteList.markerColor == R.drawable.ic_pink_marker)
            selectMarker(5)

        markerContainers.forEachIndexed { index, frameLayout ->
            frameLayout.setOnClickListener {
                selectMarker(index)
            }
        }
    }

    private fun selectMarker(selectedIndex: Int) {
        markerContainers.forEachIndexed { index, frameLayout ->
            if (index == selectedIndex) {
                frameLayout.elevation = 20f // 그림자 높이
                frameLayout.setBackgroundResource(R.drawable.shadow_marker_background)
            } else {
                frameLayout.elevation = 0f
                frameLayout.setBackgroundResource(0) // 배경 제거
            }
        }
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