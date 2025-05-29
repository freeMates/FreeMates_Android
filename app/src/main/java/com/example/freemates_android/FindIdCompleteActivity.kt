package com.example.freemates_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freemates_android.databinding.ActivityFindIdCompleteBinding
import com.example.freemates_android.databinding.ActivityLoginBinding

class FindIdCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindIdCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFindIdCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val activityName = intent.getStringExtra("activity")

        if(activityName == "findId") {
            binding.tvFindIdPromptFindIdComplete.text = "아이디 찾기"
            binding.tvFindIdPrompt1FindIdComplete.text = "아이디는"
            binding.tvUserIdFindIdComplete.visibility = View.VISIBLE
            binding.tvFindIdPrompt2FindIdComplete.visibility = View.VISIBLE
            binding.tvChangePasswordFindIdComplete.text = "비밀번호 변경"
        }
        else if(activityName == "changePasswordComplete") {
            binding.tvFindIdPromptFindIdComplete.text = "비밀번호 변경"
            binding.tvFindIdPrompt1FindIdComplete.text = "비밀번호가 변경되었습니다."
            binding.tvUserIdFindIdComplete.visibility = View.INVISIBLE
            binding.tvFindIdPrompt2FindIdComplete.visibility = View.INVISIBLE
            binding.tvChangePasswordFindIdComplete.text = "아이디 찾기"
        }


        binding.tvChangePasswordFindIdComplete.setOnClickListener {
            val intent: Intent
            if(activityName == "findId")
                intent = Intent(this, ChangePasswordActivity::class.java)
            else
                intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnGotoLoginFindIdComplete.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}