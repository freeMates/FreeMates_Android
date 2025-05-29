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
import com.example.freemates_android.databinding.ActivityChangePasswordBinding
import com.example.freemates_android.databinding.ActivityFindIdBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private var isEmailVerified = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackToLoginChangePassword.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etUserEmailChangePassword.addTextChangedListener {
            binding.btnVerificationCodeCheckChangePassword.isSelected = false
            binding.btnUserEmailVerifyChangePassword.isSelected = true
        }

        binding.btnUserEmailVerifyChangePassword.setOnClickListener {
            if(binding.etUserEmailChangePassword.text.isNotEmpty()) {
                submitUserPhoneVerify()
                isEmailVerified = false
            }
        }

        binding.btnVerificationCodeCheckChangePassword.setOnClickListener {
            if(binding.etVerificationCodeChangePassword.text.isNotEmpty()) {
                if (binding.btnVerificationCodeCheckChangePassword.isSelected) {
                    submitUserVerificationCode()
                } else
                    showToast("인증하기 버튼을 눌러주세요")
            }
        }

        binding.tvFindIdChangePassword.setOnClickListener {
            val intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCompleteChangePasswordChangePassword.setOnClickListener {
            submitChangePassword()
        }
    }

    private fun submitUserPhoneVerify(){
        // TODO : API 호출

        binding.tvVerificationCodeErrorChangePassword.visibility = View.INVISIBLE
        binding.tvVerificationCodeOkayChangePassword.visibility = View.INVISIBLE
        binding.btnVerificationCodeCheckChangePassword.isSelected = true

        showToast("인증번호를 전송했습니다.")
    }

    private fun submitUserVerificationCode(){
        // TODO : API 호출

        // TODO : 나중에 값 변경!
        val state = true

        isEmailVerified = state

        if(isEmailVerified) {
            binding.tvVerificationCodeErrorChangePassword.visibility = View.INVISIBLE
            binding.tvVerificationCodeOkayChangePassword.visibility = View.VISIBLE
        }
        else {
            binding.tvVerificationCodeErrorChangePassword.visibility = View.VISIBLE
            binding.tvVerificationCodeOkayChangePassword.visibility = View.INVISIBLE
        }

        updateRegisterButtonState()
    }

    private fun updateRegisterButtonState() {
        val isReady = isEmailVerified
        binding.btnCompleteChangePasswordChangePassword.isSelected = isReady
    }

    private fun submitChangePassword(){
        if(binding.btnCompleteChangePasswordChangePassword.isSelected) {
            if(isUserIdExist()) {
                // TODO : 회원가입 API 호출

                val intent = Intent(this, ChangePasswordCompleteActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                showToast("아이디가 존재하지 않습니다.")
            }
        }
    }

    private fun isUserIdExist(): Boolean{
        if(binding.etUserIdChangePassword.text.isEmpty())
            return false
        // TODO : 서버로 아이디 전송

        val existState = true
        return existState
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}