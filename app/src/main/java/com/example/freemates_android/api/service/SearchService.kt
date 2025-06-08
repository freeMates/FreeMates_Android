package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.CategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {
    @GET("search/place")
    fun searchPlace(
        @Header("Authorization") authorization: String?,
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<CategoryResponse>

}