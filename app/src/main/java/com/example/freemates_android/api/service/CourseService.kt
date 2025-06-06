package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.CourseDto
import com.example.freemates_android.api.dto.CourseResponse
import com.example.freemates_android.api.dto.PlaceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CourseService {

    @GET("course/list")
    fun getCourseList(
        @Header("Authorization") authorization: String?,
        @Query("visibility") visibility: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<CourseResponse>

}