package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.CategoryResponse
import com.example.freemates_android.api.dto.LoginRequest
import com.example.freemates_android.api.dto.LoginResponse
import com.example.freemates_android.api.dto.MailSendResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PlaceService {
    @POST("place/refresh")
    fun placeRefresh(
        @Query("mail") email: String
    ): Call<ResponseBody>

    @GET("place/category")
    fun placeCategory(
        @Header("refreshToken") refreshToken: String,
        @Query("category") category: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): CategoryResponse
}