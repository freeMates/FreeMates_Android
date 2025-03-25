package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityLoginBinding
import com.example.freemates_android.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isIdChecked = false
    private var isPhoneVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackToLoginRegister.setOnClickListener {
            finish()
        }

        binding.btnUserIdDuplicationRegister.setOnClickListener {

        }

        binding.btnCompleteRegisterRegister.setOnClickListener {
//            submitUserInfo()
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnUserIdDuplicationRegister.setOnClickListener {
            val id = binding.etUserIdRegister.text.toString()
            if (id.length > 8) {
                showToast("아이디는 8자 이하여야 합니다.")
                return@setOnClickListener
            }
            // TODO: 아이디 중복 확인 API 호출
            isIdChecked = true
        }

        binding.btnUserPhoneVerifyRegister.setOnClickListener {
            val phone = binding.etUserPhoneRegister.text.toString()
            // TODO: 인증번호 전송 API
        }

        binding.btnVerificationCodeCheckRegister.setOnClickListener {
            val code = binding.etVerificationCodeRegister.text.toString()
            // TODO: 인증번호 확인 API
            isPhoneVerified = true
        }

        binding.btnCompleteRegisterRegister.setOnClickListener {
            if (!isIdChecked) {
                showToast("아이디 중복 확인을 해주세요.")
                return@setOnClickListener
            }

            if (!isPhoneVerified) {
                showToast("전화번호 인증을 완료해주세요.")
                return@setOnClickListener
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}