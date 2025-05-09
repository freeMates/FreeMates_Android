package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityFindIdBinding
import com.example.freemates_android.databinding.ActivityLoginBinding

class FindIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindIdBinding
    private var isPhoneVerified = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackToLoginFindId.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etUserPhoneFindId.addTextChangedListener {
            binding.btnVerificationCodeCheckFindId.isSelected = false
        }

        binding.btnUserPhoneVerifyFindId.setOnClickListener {
            submitUserPhoneVerify()
            isPhoneVerified = false
        }

        binding.btnVerificationCodeCheckFindId.setOnClickListener {
            if(binding.btnVerificationCodeCheckFindId.isSelected) {
                submitUserVerificationCode()
            }
            else
                showToast("인증하기 버튼을 눌러주세요")
        }

        binding.tvChangePasswordFindId.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegisterFindId.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCompleteFindIdFindId.setOnClickListener {
            submitFindId()
        }
    }

    private fun submitUserPhoneVerify(){
        val phone = binding.etUserPhoneFindId.text.toString().trim()

        if (!phone.matches(Regex("^01[0-9]{8,9}$"))) {
            showToast("올바른 전화번호 형식을 입력해주세요.")
            binding.btnVerificationCodeCheckFindId.isSelected = false
            return
        }

        // TODO : API 호출

        binding.tvVerificationCodeErrorFindId.visibility = View.INVISIBLE
        binding.btnVerificationCodeCheckFindId.isSelected = true

        showToast("인증번호를 전송했습니다.")
    }

    private fun submitUserVerificationCode(){
        // TODO : API 호출

        // TODO : 나중에 값 변경!
        val state = true

        isPhoneVerified = state

        if(isPhoneVerified)
            binding.tvVerificationCodeErrorFindId.visibility = View.INVISIBLE
        else
            binding.tvVerificationCodeErrorFindId.visibility = View.VISIBLE

        updateRegisterButtonState()
    }

    private fun updateRegisterButtonState() {
        val isReady = isPhoneVerified
        binding.btnCompleteFindIdFindId.isSelected = isReady
    }

    private fun submitFindId(){
        if(binding.btnCompleteFindIdFindId.isSelected) {
            // TODO : 회원가입 API 호출

            val intent = Intent(this, FindIdCompleteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}