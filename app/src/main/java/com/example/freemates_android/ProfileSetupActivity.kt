package com.example.freemates_android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityLoginBinding
import com.example.freemates_android.databinding.ActivityProfileSetupBinding

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private var selectedGender: String? = null

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
        if (!isValidProfileInput()) {
            Toast.makeText(this, "모든 정보를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO : 서버에 저장

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

        val color = if (isValid)
            ContextCompat.getColor(this, R.color.black)
        else
            ContextCompat.getColor(this, R.color.unselected_color)

        binding.btnCompleteProfileProfileSetup.setTextColor(color)
    }
}