package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityLoginBinding
import com.example.freemates_android.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isIdChecked = false
    private var isPhoneVerified = false
    private var isPasswordVisible = false
    private var isPasswordConfirmVisible = false

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

        // 기본값 설정
        binding.etUserPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.etUserPasswordCheckRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


        binding.btnBackToLoginRegister.setOnClickListener {
            finish()
        }

        binding.etUserIdRegister.addTextChangedListener {
            if(binding.etUserIdRegister.text.isNotEmpty())
                binding.btnUserIdDuplicationRegister.isSelected = true
            else
                binding.btnUserIdDuplicationRegister.isSelected = false

            isIdChecked = false

            binding.tvUserIdAvailabilityRegister.visibility = View.INVISIBLE
            binding.tvUserIdLengthErrorRegister.visibility = View.INVISIBLE
            binding.tvUserIdDuplicateErrorRegister.visibility = View.INVISIBLE
        }

        binding.btnUserIdDuplicationRegister.setOnClickListener {
            submitUserIdDuplicate()
            updateRegisterButtonState()
        }

        binding.etUserPasswordRegister.addTextChangedListener {
            updatePasswordErrorTextState()
            updateRegisterButtonState()
        }

        binding.etUserPasswordCheckRegister.addTextChangedListener {
            updatePasswordCheckErrorTextState()
            updateRegisterButtonState()
        }

        binding.etUserEmailRegister.addTextChangedListener {
            if(binding.etUserEmailRegister.text.isNotEmpty()){
                binding.btnUserEmailVerifyRegister.isSelected = true
                binding.btnVerificationCodeCheckRegister.isSelected = false
            } else{
                binding.btnUserEmailVerifyRegister.isSelected = false
                binding.btnVerificationCodeCheckRegister.isSelected = false
            }
        }

        binding.btnUserEmailVerifyRegister.setOnClickListener {
            submitUserPhoneVerify()
            isPhoneVerified = false
        }

        binding.etVerificationCodeRegister.addTextChangedListener {
            if(binding.etVerificationCodeRegister.text.isNotEmpty())
                binding.btnVerificationCodeCheckRegister.isSelected = true
            else
                binding.btnVerificationCodeCheckRegister.isSelected = false
        }

        binding.btnVerificationCodeCheckRegister.setOnClickListener {
            if(binding.btnVerificationCodeCheckRegister.isSelected) {
                submitUserVerificationCode()
            }
            else
                showToast("인증하기 버튼을 눌러주세요")
        }

        binding.btnCompleteRegisterRegister.setOnClickListener {
            updatePasswordErrorTextState()
            updatePasswordCheckErrorTextState()
            submitUserInfo()
        }

        setupPasswordVisibilityToggles()
//        setupListeners()
    }

    private fun submitUserIdDuplicate(){
        // TODO : API 연결

        // TODO : API 값에 따라 변경
        val duplicateState = "success"

        if(duplicateState == "success"){
            binding.tvUserIdAvailabilityRegister.visibility = View.VISIBLE
            binding.tvUserIdLengthErrorRegister.visibility = View.INVISIBLE
            binding.tvUserIdDuplicateErrorRegister.visibility = View.INVISIBLE

            isIdChecked = true
        } else if(duplicateState == "length"){
            binding.tvUserIdAvailabilityRegister.visibility = View.INVISIBLE
            binding.tvUserIdLengthErrorRegister.visibility = View.VISIBLE
            binding.tvUserIdDuplicateErrorRegister.visibility = View.INVISIBLE

            isIdChecked = false
        } else if(duplicateState == "duplicate"){
            binding.tvUserIdAvailabilityRegister.visibility = View.INVISIBLE
            binding.tvUserIdLengthErrorRegister.visibility = View.INVISIBLE
            binding.tvUserIdDuplicateErrorRegister.visibility = View.VISIBLE

            isIdChecked = false
        }
    }

    private fun setupPasswordVisibilityToggles() {
        binding.etUserPasswordRegister.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etUserPasswordRegister.compoundDrawablesRelative[2]
                drawableEnd?.let {
                    val drawableWidth = it.bounds.width()
                    val touchArea = binding.etUserPasswordRegister.width - binding.etUserPasswordRegister.paddingEnd - drawableWidth
                    if (event.x > touchArea) {
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.etUserPasswordCheckRegister.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.etUserPasswordCheckRegister.compoundDrawablesRelative[2]
                drawableEnd?.let {
                    val drawableWidth = it.bounds.width()
                    val touchArea = binding.etUserPasswordCheckRegister.width - binding.etUserPasswordCheckRegister.paddingEnd - drawableWidth
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
        val selection = binding.etUserPasswordRegister.selectionEnd
        if (isPasswordVisible) {
            binding.etUserPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_off), null)
        } else {
            binding.etUserPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_on), null)
        }
        isPasswordVisible = !isPasswordVisible
        binding.etUserPasswordRegister.setSelection(selection)
    }

    private fun togglePasswordConfirmVisibility() {
        val selection = binding.etUserPasswordCheckRegister.selectionEnd
        if (isPasswordConfirmVisible) {
            binding.etUserPasswordCheckRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordCheckRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_off), null)
        } else {
            binding.etUserPasswordCheckRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordCheckRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_password_visibility_on), null)
        }
        isPasswordConfirmVisible = !isPasswordConfirmVisible
        binding.etUserPasswordCheckRegister.setSelection(selection)
    }

    private fun isValidPassword(): Boolean {
        val pw = binding.etUserPasswordRegister.text.toString()
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
        return binding.etUserPasswordRegister.text.toString() == binding.etUserPasswordCheckRegister.text.toString()
    }

    /**
     * 비밀번호 오류코드 상태 변경
     */
    private fun updatePasswordErrorTextState() {
        if(!isValidPassword())
            binding.tvUserPasswordPatternErrorRegister.setTextColor(ContextCompat.getColor(this, R.color.red))
        else
            binding.tvUserPasswordPatternErrorRegister.setTextColor(ContextCompat.getColor(this, R.color.natural300))
    }

    private fun updatePasswordCheckErrorTextState(){
        if (!passwordsMatch() && binding.etUserPasswordCheckRegister.isFocused)
            binding.tvUserPasswordMatchErrorRegister.visibility = View.VISIBLE
        else
            binding.tvUserPasswordMatchErrorRegister.visibility = View.INVISIBLE
    }

    private fun updateRegisterButtonState() {
        val isReady = isIdChecked && isPhoneVerified && isValidPassword() && passwordsMatch()
        binding.btnCompleteRegisterRegister.isSelected = isReady
    }

    private fun submitUserPhoneVerify(){

        binding.btnVerificationCodeCheckRegister.isSelected = false

        // TODO : API 호출

//        binding.tvVerificationCodeErrorRegister.visibility = View.INVISIBLE
//        binding.btnVerificationCodeCheckRegister.isSelected = true

        showToast("인증번호를 전송했습니다.")
    }

    private fun submitUserVerificationCode(){
        // TODO : API 호출

        // TODO : 나중에 값 변경!
        val state = true

        isPhoneVerified = state

        if(isPhoneVerified){
            binding.tvVerificationCodeErrorRegister.visibility = View.INVISIBLE
            binding.tvVerificationCodeOkayRegister.visibility = View.VISIBLE
        }
        else{
            binding.tvVerificationCodeErrorRegister.visibility = View.VISIBLE
            binding.tvVerificationCodeOkayRegister.visibility = View.INVISIBLE
        }

        updateRegisterButtonState()
    }

    private fun submitUserInfo(){
        if(binding.btnCompleteRegisterRegister.isSelected) {
            // TODO : 회원가입 API 호출

            val intent = Intent(this, ProfileSetupActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}