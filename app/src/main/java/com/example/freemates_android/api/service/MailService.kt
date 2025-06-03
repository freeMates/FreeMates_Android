package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.LoginRequest
import com.example.freemates_android.api.dto.LoginResponse
import com.example.freemates_android.api.dto.MailSendResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MailService {
    @GET("mail/send")
    fun mailSend(
        @Query("mail") email: String
    ): Call<ResponseBody>

    @GET("mail/check-verification")
    fun mailCheckVerification(
        @Query("mail") mail: String
    ): Call<Boolean>
}