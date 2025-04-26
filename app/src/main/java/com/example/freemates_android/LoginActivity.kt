package com.example.freemates_android

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.RenderEffect
import android.graphics.RenderEffect.createBlurEffect
import android.graphics.RenderEffect.createChainEffect
import android.graphics.Shader
import android.os.Build
import android.view.ViewOutlineProvider
import androidx.core.content.ContextCompat
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freemates_android.databinding.ActivityLoginBinding

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
    }

    private fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
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
        val id = binding.etUserIdLogin.text.toString().trim()
        val pw = binding.etUserPasswordLogin.text.toString().trim()

        if (id.isEmpty() || pw.isEmpty()) {
            binding.tvUserLoginErrorLogin.visibility = View.VISIBLE
            return
        }

        performLogin(id, pw)
    }

    private fun performLogin(id: String, pw: String) {
        // TODO: Retrofit 사용하여 서버에 로그인 요청

        // TODO : 나중에 값 변경하기!
        val isSuccess = true
        if (isSuccess) {
            val intent = Intent(this, LoginCompleteActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.tvUserLoginErrorLogin.visibility = View.VISIBLE
        }
    }
}