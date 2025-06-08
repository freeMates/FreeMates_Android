package com.example.freemates_android.Activity

import android.app.Activity
import android.app.ComponentCaller
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.freemates_android.R
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.TokenManager.saveTokens
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.BookmarkCreateResponse
import com.example.freemates_android.databinding.ActivityEditFavoriteBinding
import com.example.freemates_android.model.map.FavoriteList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class EditFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFavoriteBinding
    private lateinit var favoriteList: FavoriteList
    private lateinit var pageName: String
    private lateinit var markerContainers: List<FrameLayout>
    private lateinit var pinColor: String
    private val PICK_IMAGE_REQUEST = 1001
    private var imageUri: Uri? = null

    // 선택된 이미지 Uri를 보관할 변수
    private var selectedImageUri: Uri? = null

    // MultipartBody.Part로 변환된 이미지 파트를 보관
    private var imagePart: MultipartBody.Part? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                selectedImageUri = uri
                binding.ivFavoriteImageEditFavorite.setImageURI(uri)

                // 화면 IO 작업이니 별도 쓰레드로 처리 (코루틴 활용)
                lifecycleScope.launchWhenStarted {
                    createImageMultipart(uri)
                }
            }
        }
    }

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
        pageName = intent.getStringExtra("arg_page_name").toString()

        initUI()
        clickEvent()
        textChangeEvent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("EditFavorite", "Image 변경")
                imageUri = uri
                binding.ivFavoriteImageEditFavorite.setImageURI(uri)  // 선택된 이미지를 ImageView에 표시
            }
        }
    }

    private fun initUI() {
        binding.btnCompleteEditEditFavorite.isSelected = false
        binding.btnCompleteEditEditFavorite.isClickable = false

        Glide.with(this)
            .load(favoriteList.thumbnailUrl)
            .placeholder(R.drawable.ic_image_default) // 로딩 중
            .error(R.drawable.ic_image_default)       // 404 등 에러
            .fallback(R.drawable.ic_image_default)
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

        if (favoriteList.markerColor == R.drawable.ic_red_marker) {
            selectMarker(0)
            pinColor = "RED"
        } else if (favoriteList.markerColor == R.drawable.ic_yellow_marker) {
            selectMarker(1)
            pinColor = "YELLOW"
        } else if (favoriteList.markerColor == R.drawable.ic_green_marker) {
            selectMarker(2)
            pinColor = "GREEN"
        } else if (favoriteList.markerColor == R.drawable.ic_darkblue_marker) {
            selectMarker(3)
            pinColor = "BLUE"
        } else if (favoriteList.markerColor == R.drawable.ic_purple_marker) {
            selectMarker(4)
            pinColor = "PURPLE"
        } else if (favoriteList.markerColor == R.drawable.ic_pink_marker) {
            selectMarker(5)
            pinColor = "PINK"
        }

        markerContainers.forEachIndexed { index, frameLayout ->
            frameLayout.setOnClickListener {
                selectMarker(index)
            }
        }

        if (pageName == "favoriteDetail") {
            binding.btnCompleteEditEditFavorite.text = "수정하기"
        } else {
            binding.btnCompleteEditEditFavorite.text = "완료"
        }
    }

    private fun selectMarker(selectedIndex: Int) {
        markerContainers.forEachIndexed { index, frameLayout ->
            if (index == selectedIndex) {
                frameLayout.elevation = 20f // 그림자 높이
                frameLayout.setBackgroundResource(R.drawable.shadow_marker_background)

                pinColor = when (index) {
                    0 -> "RED"
                    1 -> "YELLOW"
                    2 -> "GREEN"
                    3 -> "BLUE"
                    4 -> "PURPLE"
                    5 -> "PINK"
                    else -> "UNKNOWN"
                }
            } else {
                frameLayout.elevation = 0f
                frameLayout.setBackgroundResource(0) // 배경 제거
            }
        }
    }

    private lateinit var image: MultipartBody.Part

    private fun clickEvent() {
        binding.btnBackToMapEditFavorite.setOnClickListener {
            finish()
        }

        binding.ivFavoriteImageEditFavorite.setOnClickListener {
            openGallery()
        }

        binding.btnFavoriteVisibilityPublicEditFavorite.setOnClickListener {
            selectVisibility("공개")
        }

        binding.btnFavoriteVisibilityPrivateEditFavorite.setOnClickListener {
            selectVisibility("비공개")
        }

        binding.btnCompleteEditEditFavorite.setOnClickListener {
            Log.d("EditFavorite", "완료버튼 클릭")
            val title = binding.etFavoriteTitleEditFavorite.text.toString()
            val description = binding.etFavoriteDescriptionEditFavorite.text.toString()
            val visibility =
                if (binding.btnFavoriteVisibilityPublicEditFavorite.isSelected) "PUBLIC" else "PRIVATE"

            val finalImagePart: MultipartBody.Part = if (imagePart != null) {
                imagePart!!
            } else {
                // drawable 에 있는 기본 이미지를 사용
                createMultipartFromDrawable(
                    resId = R.drawable.image2, // ← 프로젝트에 있는 기본 이미지 리소스 ID로 변경
                    fieldName = "image",                     // ← 서버 API 문서에 맞춰 필드명 설정
                    fileName = "default.jpg"                // ← 서버에 전달할 파일 이름 설정
                )
            }

            val titleBody: RequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody: RequestBody =
                description.toRequestBody("text/plain".toMediaTypeOrNull())
            val pinColorBody: RequestBody = pinColor.toRequestBody("text/plain".toMediaTypeOrNull())
            val visibilityBody: RequestBody =
                visibility.toRequestBody("text/plain".toMediaTypeOrNull())

            lifecycleScope.launch {
                val refreshToken = getRefreshToken()
                RetrofitClient.bookmarkService.createBookmark(
                    "Bearer $refreshToken",
                    titleBody,
                    descriptionBody,
                    pinColorBody,
                    visibilityBody,
                    finalImagePart
                ).enqueue(object :
                    Callback<BookmarkCreateResponse> {
                    override fun onResponse(
                        call: Call<BookmarkCreateResponse>,
                        response: Response<BookmarkCreateResponse>
                    ) {
                        if (response.code() == 201) {
                            Log.d("EditFavorite", "즐겨찾기 추가 성공")
                            finish()

                        } else {
                            val errorCode = response.errorBody()?.string()
                            Log.e("EditFavorite", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }

                    override fun onFailure(
                        call: Call<BookmarkCreateResponse>,
                        t: Throwable
                    ) {
                        val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                        Log.d("EditFavorite", value)
                    }
                })
            }
        }
    }

    private fun createMultipartFromDrawable(
        resId: Int,
        fieldName: String,
        fileName: String
    ): MultipartBody.Part {
        // 1) Drawable → Bitmap
        val bitmap = BitmapFactory.decodeResource(resources, resId)
        // 2) Bitmap → ByteArray (JPEG 압축)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val imageBytes = baos.toByteArray()
        // 3) ByteArray → RequestBody
        val requestFile = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        // 4) MultipartBody.Part 생성 (필드명은 서버 API 스펙에 맞춤)
        return MultipartBody.Part.createFormData(fieldName, fileName, requestFile)
    }


    private fun openGallery() {
        Log.d("EditFavorite", "갤러리 오픈")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private suspend fun createImageMultipart(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val cr: ContentResolver = contentResolver
            // MIME 타입을 얻어오거나, 기본값을 image/*
            val mimeType = cr.getType(uri) ?: "image/*"
            val inputStream: InputStream = cr.openInputStream(uri)
                ?: throw FileNotFoundException("Cannot open input stream from URI")
            val bytes = inputStream.use { it.readBytes() }

            // 파일명 추출 (OpenableColumns.DISPLAY_NAME 사용)
            val fileName = queryFileName(uri) ?: "image_${System.currentTimeMillis()}.jpg"
            val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

            // 서버에서 기대하는 필드명(여기서는 "image")을 꼭 확인해야 함
            imagePart = MultipartBody.Part.createFormData(
                "image",   // ← 반드시 서버 API 문서를 참고하여 필드명에 맞추기
                fileName,
                requestFile
            )
            Log.d("EditFavorite", "이미지 Multipart 준비 완료: $fileName, mimeType=$mimeType")
        } catch (e: Exception) {
            Log.e("EditFavorite", "이미지 Multipart 생성 오류", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@EditFavoriteActivity,
                    "이미지를 처리하는 도중에 오류가 발생했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun queryFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) {
                        result = it.getString(idx)
                    }
                }
            }
        }
        // content 프로토콜이 아니라거나, 위에서 못 얻었다면 경로 마지막 부분을 잘라서 파일명으로 사용
        if (result == null) {
            val path = uri.path ?: return null
            val cut = path.lastIndexOf('/')
            if (cut != -1 && cut < path.length - 1) {
                result = path.substring(cut + 1)
            }
        }
        return result
    }

    private fun textChangeEvent() {
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
        val isValid: Boolean =
            binding.etFavoriteTitleEditFavorite.length() > 0 && binding.etFavoriteDescriptionEditFavorite.length() > 0
        binding.btnCompleteEditEditFavorite.isSelected = isValid
        binding.btnCompleteEditEditFavorite.isClickable = isValid
    }
}