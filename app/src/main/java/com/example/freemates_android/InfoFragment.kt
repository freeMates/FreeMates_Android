package com.example.freemates_android

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.freemates_android.UserInfoManager.getAgeInfo
import com.example.freemates_android.UserInfoManager.getEmailInfo
import com.example.freemates_android.UserInfoManager.getGenderInfo
import com.example.freemates_android.UserInfoManager.getNicknameInfo
import com.example.freemates_android.UserInfoManager.getPasswordInfo
import com.example.freemates_android.UserInfoManager.getUserNameInfo
import com.example.freemates_android.databinding.FragmentInfoBinding
import kotlinx.coroutines.launch

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInfoBinding.bind(view)

        lifecycleScope.launch {
            binding.etUserIdInfo.setText(requireContext().getUserNameInfo())
            binding.etUserPasswordInfo.setText(requireContext().getPasswordInfo())
            binding.etUserEmailInfo.setText(requireContext().getEmailInfo())
            binding.etUserNameInfo.setText(requireContext().getNicknameInfo())
            binding.etUserAgeInfo.setText("${requireContext().getAgeInfo()}살")
            val gender = when(requireContext().getGenderInfo()) {
                "MALE" -> "남성"
                else -> "여성"
            }
            binding.etUserGenderInfo.setText(gender)
        }
    }
}