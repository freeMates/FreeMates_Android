package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.CourseDto
import com.example.freemates_android.api.dto.CourseRequest
import com.example.freemates_android.api.dto.CourseResponse
import com.example.freemates_android.api.dto.PlaceDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("course/create")
    fun createCourse(
        @Header("Authorization") authorization: String?,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("freeTime") freeTime: RequestBody,
        @Part("visibility") visibility: RequestBody,
        @Part("placeIds") placeIds: List<@JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Call<CourseResponse>

}