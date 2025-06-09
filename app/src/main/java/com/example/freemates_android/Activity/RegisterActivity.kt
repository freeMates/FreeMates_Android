package com.example.freemates_android.Activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.LoadingDialog
import com.example.freemates_android.R
import com.example.freemates_android.api.RetrofitClient
import com.example.freemates_android.api.dto.MailSendResponse
import com.example.freemates_android.databinding.ActivityRegisterBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isIdChecked = false
    private var isEmailVerified = false
    private var isPasswordVisible = false
    private var isPasswordConfirmVisible = false
    private lateinit var loadingDialog: LoadingDialog

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

        loadingDialog = LoadingDialog(this)

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
            submitUserEmailVerify()
            isEmailVerified = false
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
    }

    private fun submitUserIdDuplicate(){
        val username = binding.etUserIdRegister.text.toString()

        if(username.length > 8){
            binding.tvUserIdAvailabilityRegister.visibility = View.INVISIBLE
            binding.tvUserIdLengthErrorRegister.visibility = View.VISIBLE
            binding.tvUserIdDuplicateErrorRegister.visibility = View.INVISIBLE

            isIdChecked = false

            return
        }
        loadingDialog.showLoading()

        RetrofitClient.authService.duplicateUsername(
            username
        ).enqueue(object :
            Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                if (response.isSuccessful) {
                    Log.d("Register", "isDuplicate : ${response.body()}")
                    // 중복 x
                    if(response.body() == false){
                        binding.tvUserIdAvailabilityRegister.visibility = View.VISIBLE
                        binding.tvUserIdLengthErrorRegister.visibility = View.INVISIBLE
                        binding.tvUserIdDuplicateErrorRegister.visibility = View.INVISIBLE

                        isIdChecked = true
                    }
                    else{
                        binding.tvUserIdAvailabilityRegister.visibility = View.INVISIBLE
                        binding.tvUserIdLengthErrorRegister.visibility = View.INVISIBLE
                        binding.tvUserIdDuplicateErrorRegister.visibility = View.VISIBLE

                        isIdChecked = false
                    }
                    loadingDialog.hideLoading()
                } else {
                    val errorCode = response.errorBody()?.string()
                    Log.e("Register", "응답 실패: ${response.code()} - $errorCode")

                    loadingDialog.hideLoading()
                }
            }

            override fun onFailure(
                call: Call<Boolean>,
                t: Throwable
            ) {
                val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                Log.d("Register", value)

                loadingDialog.hideLoading()
            }
        })
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
            binding.etUserPasswordRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,
                R.drawable.ic_password_visibility_off
            ), null)
        } else {
            binding.etUserPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,
                R.drawable.ic_password_visibility_on
            ), null)
        }
        isPasswordVisible = !isPasswordVisible
        binding.etUserPasswordRegister.setSelection(selection)
    }

    private fun togglePasswordConfirmVisibility() {
        val selection = binding.etUserPasswordCheckRegister.selectionEnd
        if (isPasswordConfirmVisible) {
            binding.etUserPasswordCheckRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etUserPasswordCheckRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,
                R.drawable.ic_password_visibility_off
            ), null)
        } else {
            binding.etUserPasswordCheckRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.etUserPasswordCheckRegister.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,
                R.drawable.ic_password_visibility_on
            ), null)
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
            binding.tvUserPasswordPatternErrorRegister.setTextColor(ContextCompat.getColor(this,
                R.color.red
            ))
        else
            binding.tvUserPasswordPatternErrorRegister.setTextColor(ContextCompat.getColor(this,
                R.color.natural300
            ))
    }

    private fun updatePasswordCheckErrorTextState(){
        if (!passwordsMatch() && binding.etUserPasswordCheckRegister.isFocused)
            binding.tvUserPasswordMatchErrorRegister.visibility = View.VISIBLE
        else
            binding.tvUserPasswordMatchErrorRegister.visibility = View.INVISIBLE
    }

    private fun updateRegisterButtonState() {
        val isReady = isIdChecked && isEmailVerified && isValidPassword() && passwordsMatch()
        binding.btnCompleteRegisterRegister.isSelected = isReady
    }

    private fun submitUserEmailVerify(){
        val email = binding.etUserEmailRegister.text.toString()

        binding.btnVerificationCodeCheckRegister.isSelected = false
        loadingDialog.showLoading()

        RetrofitClient.mailService.mailSend(
            email
        ).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseText = response.body()!!.string()
                    Log.d("Register", "Response Text: $responseText")
                    loadingDialog.hideLoading()
                    showToast("메일 전송을 완료하였습니다.")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Register", "Error response: $errorBody")

                    // 에러 코드 처리
                    if (errorBody?.contains("INVALID_EMAIL") == true) {
                        showToast("유효하지 않은 이메일입니다.")
                    } else if (errorBody?.contains("DUPLICATE_EMAIL") == true) {
                        binding.tvUserIdDuplicateErrorRegister.visibility = View.VISIBLE
                        isIdChecked = false
                    } else {
                        showToast("알 수 없는 오류가 발생했습니다.")
                    }

                    loadingDialog.hideLoading()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                Log.d("Register", value)

                loadingDialog.hideLoading()
            }
        })
    }

    private fun submitUserVerificationCode(){
        val email = binding.etUserEmailRegister.text.toString()
        loadingDialog.showLoading()

        isEmailVerified = false
        RetrofitClient.mailService.mailCheckVerification(
            email
        ).enqueue(object :
            Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                if (response.isSuccessful) {
                    Log.d("Register", "isCheckVerification : ${response.body()}")
                    // 인증 o
                    if(response.body() == true){
                        binding.tvVerificationCodeErrorRegister.visibility = View.INVISIBLE
                        binding.tvVerificationCodeOkayRegister.visibility = View.VISIBLE

                        isEmailVerified = true
                    }
                    else{
                        binding.tvVerificationCodeErrorRegister.visibility = View.VISIBLE
                        binding.tvVerificationCodeOkayRegister.visibility = View.INVISIBLE

                        isEmailVerified = false
                    }
                    updateRegisterButtonState()

                    loadingDialog.hideLoading()
                } else {
                    val errorCode = response.errorBody()?.string()
                    Log.e("Register", "응답 실패: ${response.code()} - $errorCode")

                    loadingDialog.hideLoading()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                val value = "Failure: ${t.message}"  // 네트워크 오류 처리
                Log.d("Register", value)

                loadingDialog.hideLoading()
            }
        })

        updateRegisterButtonState()
    }

    private fun submitUserInfo(){
        if(binding.btnCompleteRegisterRegister.isSelected) {
            val id = binding.etUserIdRegister.text.toString()
            val password = binding.etUserPasswordRegister.text.toString()
            val email = binding.etUserEmailRegister.text.toString()

            val intent = Intent(this, ProfileSetupActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("password", password)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}