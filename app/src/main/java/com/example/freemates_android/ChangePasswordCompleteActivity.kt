package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityChangePasswordBinding
import com.example.freemates_android.databinding.ActivityChangePasswordCompleteBinding

class ChangePasswordCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordCompleteBinding
    private var isPasswordVisible = false
    private var isPasswordConfirmVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 기본값 설정
        binding.etUserPasswordChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.etUserPasswordCheckChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.btnBackToLoginChangePasswordComplete.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etUserPasswordChangePasswordComplete.addTextChangedListener {
            updatePasswordErrorTextState()
            updateChangePasswordButtonState()
        }

        binding.etUserPasswordCheckChangePasswordComplete.addTextChangedListener {
            updatePasswordCheckErrorTextState()
            updateChangePasswordButtonState()
        }

        binding.btnCompleteChangePasswordChangePassword.setOnClickListener {
            updatePasswordErrorTextState()
            updatePasswordCheckErrorTextState()
            submitChangePassword()
        }
        
        setupPasswordVisibilityToggles()
    }

    private fun setupPasswordVisibilityToggles() {
        binding.etUserPasswordChangePasswordComplete.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etUserPasswordChangePasswordComplete.compoundDrawablesRelative[2]
                drawableEnd?.let {
                    val drawableWidth = it.bounds.width()
                    val touchArea = binding.etUserPasswordChangePasswordComplete.width - binding.etUserPasswordChangePasswordComplete.paddingEnd - drawableWidth
                    if (event.x > touchArea) {
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.etUserPasswordCheckChangePasswordComplete.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etUserPasswordCheckChangePasswordComplete.compoundDrawablesRelative[2]
                drawableEnd?.let {
                    val drawableWidth = it.bounds.width()
                    val touchArea = binding.etUserPasswordCheckChangePasswordComplete.width - binding.etUserPasswordCheckChangePasswordComplete.paddingEnd - drawableWidth
                    if (event.x > touchArea) {
                        togglePasswordConfirmVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        val selection = binding.etUserPasswordChangePasswordComplete.selectionEnd
        if (isPasswordVisible) {
            binding.etUserPasswordChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordChangePasswordComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_off), null)
        } else {
            binding.etUserPasswordChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordChangePasswordComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_on), null)
        }
        isPasswordVisible = !isPasswordVisible
        binding.etUserPasswordChangePasswordComplete.setSelection(selection)
    }

    private fun togglePasswordConfirmVisibility() {
        val selection = binding.etUserPasswordCheckChangePasswordComplete.selectionEnd
        if (isPasswordConfirmVisible) {
            binding.etUserPasswordCheckChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordCheckChangePasswordComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_off), null)
        } else {
            binding.etUserPasswordCheckChangePasswordComplete.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordCheckChangePasswordComplete.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_on), null)
        }
        isPasswordConfirmVisible = !isPasswordConfirmVisible
        binding.etUserPasswordCheckChangePasswordComplete.setSelection(selection)
    }

    private fun isValidPassword(): Boolean {
        val pw = binding.etUserPasswordChangePasswordComplete.text.toString()
        val lengthOk = pw.length >= 10
        val types = listOf(
            Regex("[A-Z]"), Regex("[a-z]"),
            Regex("[0-9]"), Regex("[^A-Za-z0-9]"),
        ).count { it.containsMatchIn(pw) }
        return lengthOk && types >= 2
    }

    /**
     * 비밀번호와 비밀번호 확인의 문자가 같은지 확인
     */
    private fun passwordsMatch(): Boolean {
        return binding.etUserPasswordChangePasswordComplete.text.toString() == binding.etUserPasswordCheckChangePasswordComplete.text.toString()
    }

    /**
     * 비밀번호 오류코드 상태 변경
     */
    private fun updatePasswordErrorTextState() {
        if(!isValidPassword())
            binding.tvUserPasswordPatternErrorChangePasswordComplete.setTextColor(ContextCompat.getColor(this, R.color.red))
        else
            binding.tvUserPasswordPatternErrorChangePasswordComplete.setTextColor(ContextCompat.getColor(this, R.color.natural300))
    }

    private fun updatePasswordCheckErrorTextState(){
        if (!passwordsMatch() && binding.etUserPasswordCheckChangePasswordComplete.isFocused)
            binding.tvUserPasswordMatchErrorChangePasswordComplete.visibility = View.VISIBLE
        else
            binding.tvUserPasswordMatchErrorChangePasswordComplete.visibility = View.INVISIBLE
    }

    private fun updateChangePasswordButtonState() {
        val isReady = isValidPassword() && passwordsMatch()
        binding.btnCompleteChangePasswordChangePassword.isSelected = isReady
    }

    private fun submitChangePassword(){
        if(binding.btnCompleteChangePasswordChangePassword.isSelected) {
            // TODO : 비밀번호 변경 API 호출

            val intent = Intent(this, FindIdCompleteActivity::class.java)
            intent.putExtra("activity", "changePasswordComplete")
            startActivity(intent)
            finish()
        }
    }
}