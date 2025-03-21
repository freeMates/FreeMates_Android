package com.example.freemates_android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.freemates_android.databinding.ActivityLoginBinding
import com.example.freemates_android.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.etUserPasswordRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (text != null) {
//                    binding.tilUserPassword.isHintEnabled = text.isEmpty()
//                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}