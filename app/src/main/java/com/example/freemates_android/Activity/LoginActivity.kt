package com.example.freemates_android.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.R
import com.example.freemates_android.api.dto.LoginRequest
import com.example.freemates_android.api.dto.LoginResponse
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 기본값 설정
        binding.etUserPasswordLogin.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.btnLoginLogin.setOnClickListener {
            if(binding.btnLoginLogin.isSelected)
                attemptLogin()
        }

        binding.tvFindIdLogin.setOnClickListener {
            val intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
        }

        binding.etUserPasswordLogin.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etUserPasswordLogin.compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val clickAreaStart = binding.etUserPasswordLogin.width - binding.etUserPasswordLogin.paddingEnd - drawableWidth

                    if (event.x > clickAreaStart) {
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.tvRegisterLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.etUserIdLogin.addTextChangedListener {
            val id = binding.etUserIdLogin.text.toString().trim()
            val pw = binding.etUserPasswordLogin.text.toString().trim()

            binding.btnLoginLogin.isSelected = id.isNotEmpty() && pw.isNotEmpty()

            binding.tvUserLoginErrorLogin.visibility = View.INVISIBLE
        }

        binding.etUserPasswordLogin.addTextChangedListener {
            val id = binding.etUserIdLogin.text.toString().trim()
            val pw = binding.etUserPasswordLogin.text.toString().trim()

            binding.btnLoginLogin.isSelected = id.isNotEmpty() && pw.isNotEmpty()

            binding.tvUserLoginErrorLogin.visibility = View.INVISIBLE
        }
    }

    private fun togglePasswordVisibility() {
        val selection = binding.etUserPasswordLogin.selectionEnd

        if (isPasswordVisible) {
            // 비밀번호 숨기기
            binding.etUserPasswordLogin.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_off), null
            )
        } else {
            // 비밀번호 보이기
            binding.etUserPasswordLogin.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_on), null
            )
        }

        isPasswordVisible = !isPasswordVisible
        binding.etUserPasswordLogin.setSelection(selection) // 커서 위치 복구
    }

    private fun attemptLogin() {
        val id = binding.etUserIdLogin.text.toString()
        val pw = binding.etUserPasswordLogin.text.toString()

        RetrofitClient.authService.loginApp(
            LoginRequest(id, pw)
        ).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val value = response.body()
                    Log.d("Login", "로그인 성공!")
                    if (value != null) {
                        Log.d("Login", "아이디 : ${value.nickname}")
                    }

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    val errorCode = response.errorBody()?.string()
                    Log.e("Login", "응답 실패: ${response.code()} - $errorCode")
                    binding.tvUserLoginErrorLogin.apply {
                        text = "아이디 및 비밀번호가 맞지 않습니다."
                        visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                Log.d("Login", value)
                binding.tvUserLoginErrorLogin.apply {
                    text = "네트워크 오류가 발생했습니다."
                    visibility = View.VISIBLE
                }
            }
        })
    }
}