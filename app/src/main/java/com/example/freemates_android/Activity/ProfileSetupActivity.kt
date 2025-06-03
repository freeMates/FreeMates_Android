package com.example.freemates_android.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.R
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.RegisterRequest
import com.example.freemates_android.api.dto.RegisterResponse
import com.example.freemates_android.databinding.ActivityProfileSetupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private var selectedGender: String? = null

    private lateinit var id: String
    private lateinit var password: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        id = intent.getStringExtra("id").toString()
        password = intent.getStringExtra("password").toString()
        email = intent.getStringExtra("email").toString()

        Log.d("ProfileSetup", "id : $id")
        Log.d("ProfileSetup", "password : $password")
        Log.d("ProfileSetup", "email : $email")

        // 기본값 설정
        binding.btnMaleProfileSetup.isSelected = true

        binding.etUserNicknameProfileSetup.addTextChangedListener {
            binding.tvNicknameErrorProfileSetup.visibility =
                if (isValidNickname()) View.INVISIBLE else View.VISIBLE

            updateCompleteButtonState()
        }

        binding.etUserAgeProfileSetup.addTextChangedListener {
            updateCompleteButtonState()
        }

        binding.btnBackToRegisterProfileSetup.setOnClickListener {
            finish()
        }

        binding.btnMaleProfileSetup.setOnClickListener {
            selectGender("남성")
        }

        binding.btnFemaleProfileSetup.setOnClickListener {
            selectGender("여성")
        }

        binding.btnCompleteProfileProfileSetup.setOnClickListener {
            submitProfileInfo()
        }
    }

    /**
     * 성별 선택 함수
     */
    private fun selectGender(gender: String) {
        selectedGender = gender

        when (gender) {
            "남성" -> {
                binding.btnMaleProfileSetup.isSelected = true
                binding.btnFemaleProfileSetup.isSelected = false
            }
            "여성" -> {
                binding.btnMaleProfileSetup.isSelected = false
                binding.btnFemaleProfileSetup.isSelected = true
            }
        }

        updateCompleteButtonState()
    }

    /**
     * 입력된 프로필 정보 제출
     */
    private fun submitProfileInfo(){
        val nickname = binding.etUserNicknameProfileSetup.text.toString()
        val age = binding.etUserAgeProfileSetup.text.toString()
        val gender: String
        gender = if(binding.btnMaleProfileSetup.isSelected)
            "MALE"
        else
            "FEMALE"

        if (!isValidProfileInput()) {
            Toast.makeText(this, "모든 정보를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.authService.register(
            RegisterRequest(
                id,
                password,
                age.toInt(),
                gender,
                email,
                nickname,
            )
        ).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                when (response.code()) {
                    201 -> {
                        Log.d("ProfileSetup", "isCheckVerification : ${response.body()}")

                        val intent =
                            Intent(this@ProfileSetupActivity, RegisterCompleteActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    else -> {
                        val errorCode = response.errorBody()?.string()
                        Log.e("ProfileSetup", "응답 실패: ${response.code()} - $errorCode")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                Log.d("ProfileSetup", value)
            }
        })
    }

    /**
     * 닉네임 유효성 검사 확인
     */
    private fun isValidNickname(): Boolean {
        val nickname = binding.etUserNicknameProfileSetup.text.toString()
        return nickname.isNotEmpty() && nickname.length <= 8
    }

    /**
     * 나이 유효성 검사
     */
    private fun isValidAge(): Boolean {
        val age = binding.etUserAgeProfileSetup.text.toString().trim()
        return age.isNotEmpty() && age.toIntOrNull() != null && age.toInt() in 1..150
    }

    /**
     * 전체 프로필 입력 유효성 검사
     */
    private fun isValidProfileInput(): Boolean {
        return isValidNickname() && isValidAge()
    }

    /**
     * 완료 버튼 상태 설정
     */
    private fun updateCompleteButtonState() {
        val isValid = isValidProfileInput()
        binding.btnCompleteProfileProfileSetup.isSelected = isValid
        binding.btnCompleteProfileProfileSetup.isClickable = isValid
    }
}