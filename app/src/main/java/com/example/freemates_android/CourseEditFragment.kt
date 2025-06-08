package com.example.freemates_android

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.freemates_android.TokenManager.getRefreshToken
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.BookmarkCreateResponse
import com.example.freemates_android.api.dto.CourseRequest
import com.example.freemates_android.api.dto.CourseResponse
import com.example.freemates_android.databinding.FragmentCourseEditBinding
import com.example.freemates_android.databinding.FragmentCourseInfoBinding
import com.example.freemates_android.model.Category
import com.example.freemates_android.model.CategoryItem
import com.example.freemates_android.model.Course
import com.example.freemates_android.model.CourseInfo
import com.example.freemates_android.model.FilterItem
import com.example.freemates_android.model.RecommendItem
import com.example.freemates_android.ui.adapter.course.CourseEditAdapter
import com.example.freemates_android.ui.adapter.course.CourseInfoAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class CourseEditFragment : Fragment(R.layout.fragment_course_edit) {

    private lateinit var binding: FragmentCourseEditBinding
    private lateinit var course: Course
    private var courseDuration: Int = 0
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
            result.data?.data?.let { uri ->
                selectedImageUri = uri          // 1) 상태 보존용
                Log.d("CourseEdit", "선택된 URI = $uri")

                // 2) 화면 즉시 반영 – Glide 이용, 캐시 우회
                Glide.with(this@CourseEditFragment)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_image_default)
                    .error(R.drawable.ic_image_default)
                    .fallback(R.drawable.ic_image_default)
                    .into(binding.ivCourseImageCourseEdit)

                // 3) IO 스레드에서 multipart 생성
                lifecycleScope.launchWhenStarted {
                    createImageMultipart(uri)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCourseEditBinding.bind(view)

        initUI()
        clickEvent()
        changeButtonState()

        binding.etCourseDescriptionCourseEdit.doOnTextChanged { text, _, _, _ ->
            binding.tvCourseDescriptionLengthCourseEdit.text = "${text?.length ?: 0} / 60"
            changeButtonState()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("CourseEdit", "Image 변경")
                imageUri = uri
                binding.ivCourseImageCourseEdit.setImageURI(uri)  // 선택된 이미지를 ImageView에 표시
            }

            changeButtonState()
        }
    }

    private fun initUI(){
        course = requireArguments().getParcelable<Course>("courseInfo")!!

        Log.d("CourseEdit", "initUI check 1")
        if (course != null) {
            Log.d("CourseEdit", "initUI check : ${course.courseImage}")
            Glide.with(this)
                .load(course.courseImage)
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_image_default)
                .fallback(R.drawable.ic_image_default)
                .into(binding.ivCourseImageCourseEdit)
            Log.d("CourseEdit", "initUI check 3")
            binding.etCourseTitleCourseEdit.setText(course.courseTitle)
            Log.d("CourseEdit", "initUI check 4")
            binding.btnCourseDuration30minCourseEdit.isSelected = false
            binding.btnCourseDuration60minCourseEdit.isSelected = false
            binding.btnCourseDuration90minCourseEdit.isSelected = false
            binding.btnCourseDuration120minCourseEdit.isSelected = false
            binding.btnCourseDuration150minCourseEdit.isSelected = false
            binding.btnCourseDuration180minCourseEdit.isSelected = false

            if (course.courseDuration == "30분 소요 코스") {
                binding.btnCourseDuration30minCourseEdit.isSelected = true
                courseDuration = 30
            }
            else if (course.courseDuration == "1시간 소요 코스") {
                binding.btnCourseDuration60minCourseEdit.isSelected = true
                courseDuration = 60
            }
            else if (course.courseDuration == "1시간 30분 소요 코스") {
                binding.btnCourseDuration90minCourseEdit.isSelected = true
                courseDuration = 90
            }
            else if (course.courseDuration == "2시간 소요 코스") {
                binding.btnCourseDuration120minCourseEdit.isSelected = true
                courseDuration = 120
            }
            else if (course.courseDuration == "2시간 30분 소요 코스") {
                binding.btnCourseDuration150minCourseEdit.isSelected = true
                courseDuration = 150
            }
            else if (course.courseDuration == "3시간 소요 코스") {
                binding.btnCourseDuration180minCourseEdit.isSelected = true
                courseDuration = 180
            }

            binding.etCourseDescriptionCourseEdit.setText(course.courseDescription)

            binding.btnCourseVisibilityPublicCourseEdit.isSelected = false
            binding.btnCourseVisibilityPrivateCourseEdit.isSelected = false

            if (course.visibilityStatus)
                binding.btnCourseVisibilityPublicCourseEdit.isSelected = true
            else
                binding.btnCourseVisibilityPrivateCourseEdit.isSelected = true

            recyclerviewInit(course)
        }
    }

    private fun changeButtonState(){
        val titleState = binding.etCourseTitleCourseEdit.text.isNotEmpty()
        val descriptionState = binding.etCourseDescriptionCourseEdit.text.isNotEmpty()
        val durationState = courseDuration > 0
        val placeState = course.places.size > 1

        Log.d("CourseEdit", "titleState : $titleState")
        Log.d("CourseEdit", "descriptionState : $descriptionState")
        Log.d("CourseEdit", "durationState : $durationState")
        Log.d("CourseEdit", "placeState : $placeState")
        binding.btnCourseEditCompleteCourseEdit.isSelected = titleState && descriptionState && durationState && placeState

        Log.d("CourseEdit", "placeIds 확인 : ${course.places}")
    }

    private lateinit var courseEditAdapter: CourseEditAdapter

    private fun recyclerviewInit(courseInfo: Course){
        courseEditAdapter = CourseEditAdapter(requireContext(), courseInfo.places.toMutableList()) { viewName ->
            Log.d("CourseEdit", "viewName : $viewName")
            if(viewName == "Place") {
                Log.d("CourseEdit", "장소 검색 클릭")
                val courseDurationText = when(courseDuration) {
                    30  -> "30분 소요 코스"
                    60  -> "1시간 소요 코스"
                    90  -> "1시간 30분 소요 코스"
                    120 -> "2시간 소요 코스"
                    150 -> "2시간 30분 소요 코스"
                    else -> "3시간 소요 코스"
                }
                val currentCourseInfo = Course(
                    selectedImageUri?.toString() ?: course.courseImage,
                    binding.etCourseTitleCourseEdit.text.toString(),
                    course.courseCreator,
                    course.like,
                    course.likeCnt,
                    courseDurationText,
                    binding.etCourseDescriptionCourseEdit.text.toString(),
                    course.places,
                    course.categories,
                    binding.btnCourseVisibilityPublicCourseEdit.isSelected
                )
                val bundle = bundleOf(
                    "courseInfo" to currentCourseInfo,
                    "pageName" to "courseEdit"
                )
                findNavController().navigate(
                    R.id.action_courseEditFragment_to_searchFragment,
                    bundle
                )
            } else if(viewName == "Add") {
                val newItem = RecommendItem(
                    "",
                    "R.drawable.ic_image_default",
                    "",
                    false,
                    0,
                    "",
                    0,
                    "",
                    emptyList(),
                    "",
                    ""
                )
                courseEditAdapter.addItem(newItem)
                binding.rvCoursePlaceCourseEdit.requestLayout()
                binding.rvCoursePlaceCourseEdit.invalidate()
            }
        }

        binding.rvCoursePlaceCourseEdit.apply {
            adapter = courseEditAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun clickEvent(){
        binding.btnBackToCourseInfoCourseEdit.setOnClickListener {
            findNavController().navigate(R.id.action_courseEditFragment_to_recommendFragment)
        }

        binding.ivCourseImageCourseEdit.setOnClickListener {
            openGallery()
        }

        binding.btnCourseDuration30minCourseEdit.setOnClickListener {
            selectCourseDuration(0)
            changeButtonState()
        }

        binding.btnCourseDuration60minCourseEdit.setOnClickListener {
            selectCourseDuration(1)
            changeButtonState()
        }

        binding.btnCourseDuration90minCourseEdit.setOnClickListener {
            selectCourseDuration(2)
            changeButtonState()
        }

        binding.btnCourseDuration120minCourseEdit.setOnClickListener {
            selectCourseDuration(3)
            changeButtonState()
        }

        binding.btnCourseDuration150minCourseEdit.setOnClickListener {
            selectCourseDuration(4)
            changeButtonState()
        }

        binding.btnCourseDuration180minCourseEdit.setOnClickListener {
            selectCourseDuration(5)
            changeButtonState()
        }


        binding.btnCourseVisibilityPublicCourseEdit.setOnClickListener {
            selectVisibilityStatus("PUBLIC")
            changeButtonState()
        }

        binding.btnCourseVisibilityPrivateCourseEdit.setOnClickListener {
            selectVisibilityStatus("PRIVATE")
            changeButtonState()
        }

        binding.btnCourseEditCompleteCourseEdit.setOnClickListener {
            if(!binding.btnCourseEditCompleteCourseEdit.isSelected)
                return@setOnClickListener

            Log.d("CourseEdit", "완료버튼 클릭")
            val title = binding.etCourseTitleCourseEdit.text.toString()
            val description = binding.etCourseDescriptionCourseEdit.text.toString()
            val visibility = if(binding.btnCourseVisibilityPublicCourseEdit.isSelected) "PUBLIC" else "PRIVATE"

            val finalImagePart: MultipartBody.Part = if (imagePart != null) {
                imagePart!!
            } else {
                // drawable 에 있는 기본 이미지를 사용
                createMultipartFromDrawable(
                    resId     = R.drawable.image2, // ← 프로젝트에 있는 기본 이미지 리소스 ID로 변경
                    fieldName = "image",                     // ← 서버 API 문서에 맞춰 필드명 설정
                    fileName  = "default.jpg"                // ← 서버에 전달할 파일 이름 설정
                )
            }

            Log.d("CourseEdit", "course.places : ${course.places}")
            val placeIds = course.places.map { it.placeId }

            val titleBody: RequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody: RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val freeTimeBody: RequestBody = courseDuration.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val visibilityBody: RequestBody = visibility.toRequestBody("text/plain".toMediaTypeOrNull())
            val placeIdsBody: RequestBody = Gson().toJson(placeIds)
                .toRequestBody("application/json".toMediaTypeOrNull())

            val placeIdBodies = placeIds.map { id ->
                id.toRequestBody("text/plain".toMediaTypeOrNull())
            }

//            val request = CourseRequest(
//                title        = title,
//                description  = description,
//                freeTime     = courseDuration,
//                visibility   = visibility,
//                placeIds     = placeIds
//            )

            Log.d("CourseEdit", "placeIdBodies : $placeIds")
            lifecycleScope.launch {
                val refreshToken = requireContext().getRefreshToken()
                RetrofitClient.courseService.createCourse(
                    "Bearer $refreshToken",
                    titleBody,
                    descriptionBody,
                    freeTimeBody,
                    visibilityBody,
                    placeIdBodies,
                    finalImagePart
                ).enqueue(object :
                    Callback<CourseResponse> {
                    override fun onResponse(
                        call: Call<CourseResponse>,
                        response: Response<CourseResponse>
                    ) {
                        if (response.code() == 201) {
                            Log.d("CourseEdit", "코스 추가 성공")
                            findNavController().navigate(R.id.action_courseEditFragment_to_recommendFragment)

                        } else {
                            val errorCode = response.errorBody()?.string()
                            Log.e("CourseEdit", "응답 실패: ${response.code()} - $errorCode")
                        }
                    }

                    override fun onFailure(
                        call: Call<CourseResponse>,
                        t: Throwable
                    ) {
                        val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                        Log.d("CourseEdit", value)
                    }
                })
            }
        }
    }

    private fun selectVisibilityStatus(status: String) {
        when (status) {
            "PUBLIC" -> {
                binding.btnCourseVisibilityPublicCourseEdit.isSelected = true
                binding.btnCourseVisibilityPrivateCourseEdit.isSelected = false
            }
            "PRIVATE" -> {
                binding.btnCourseVisibilityPublicCourseEdit.isSelected = false
                binding.btnCourseVisibilityPrivateCourseEdit.isSelected = true
            }
        }
    }

    private fun selectCourseDuration(pos: Int){
        when(pos){
            0 -> {
                courseDuration = 30
                binding.btnCourseDuration30minCourseEdit.isSelected = true
                binding.btnCourseDuration60minCourseEdit.isSelected = false
                binding.btnCourseDuration90minCourseEdit.isSelected = false
                binding.btnCourseDuration120minCourseEdit.isSelected = false
                binding.btnCourseDuration150minCourseEdit.isSelected = false
                binding.btnCourseDuration180minCourseEdit.isSelected = false
            }
            1 -> {
                courseDuration = 60
                binding.btnCourseDuration30minCourseEdit.isSelected = false
                binding.btnCourseDuration60minCourseEdit.isSelected = true
                binding.btnCourseDuration90minCourseEdit.isSelected = false
                binding.btnCourseDuration120minCourseEdit.isSelected = false
                binding.btnCourseDuration150minCourseEdit.isSelected = false
                binding.btnCourseDuration180minCourseEdit.isSelected = false
            }
            2 -> {
                courseDuration = 90
                binding.btnCourseDuration30minCourseEdit.isSelected = false
                binding.btnCourseDuration60minCourseEdit.isSelected = false
                binding.btnCourseDuration90minCourseEdit.isSelected = true
                binding.btnCourseDuration120minCourseEdit.isSelected = false
                binding.btnCourseDuration150minCourseEdit.isSelected = false
                binding.btnCourseDuration180minCourseEdit.isSelected = false
            }
            3 -> {
                courseDuration = 120
                binding.btnCourseDuration30minCourseEdit.isSelected = false
                binding.btnCourseDuration60minCourseEdit.isSelected = false
                binding.btnCourseDuration90minCourseEdit.isSelected = false
                binding.btnCourseDuration120minCourseEdit.isSelected = true
                binding.btnCourseDuration150minCourseEdit.isSelected = false
                binding.btnCourseDuration180minCourseEdit.isSelected = false
            }
            4 -> {
                courseDuration = 150
                binding.btnCourseDuration30minCourseEdit.isSelected = false
                binding.btnCourseDuration60minCourseEdit.isSelected = false
                binding.btnCourseDuration90minCourseEdit.isSelected = false
                binding.btnCourseDuration120minCourseEdit.isSelected = false
                binding.btnCourseDuration150minCourseEdit.isSelected = true
                binding.btnCourseDuration180minCourseEdit.isSelected = false
            }
            5 -> {
                courseDuration = 180
                binding.btnCourseDuration30minCourseEdit.isSelected = false
                binding.btnCourseDuration60minCourseEdit.isSelected = false
                binding.btnCourseDuration90minCourseEdit.isSelected = false
                binding.btnCourseDuration120minCourseEdit.isSelected = false
                binding.btnCourseDuration150minCourseEdit.isSelected = false
                binding.btnCourseDuration180minCourseEdit.isSelected = true
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
        Log.d("CourseEdit", "갤러리 오픈")
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            // 일부 파일 관리 앱은 권한이 필요
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
//        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private suspend fun createImageMultipart(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val cr: ContentResolver = requireContext().contentResolver
            // MIME 타입을 얻어오거나, 기본값을 image/*
            val mimeType = cr.getType(uri) ?: "image/*"
            val inputStream: InputStream = cr.openInputStream(uri) ?: throw FileNotFoundException("Cannot open input stream from URI")
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
            Log.d("CourseEdit", "이미지 Multipart 준비 완료: $fileName, mimeType=$mimeType")
        } catch (e: Exception) {
            Log.e("CourseEdit", "이미지 Multipart 생성 오류", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "이미지를 처리하는 도중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun queryFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
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
}