package com.example.freemates_android.api.service

import com.example.freemates_android.api.dto.BookmarkCreateResponse
import com.example.freemates_android.api.dto.MyBookmarkListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface BookmarkService {

    @Multipart
    @POST("bookmark/create")
    fun createBookmark(
        @Header("Authorization") authorization: String?,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("pinColor") pinColor: RequestBody,
        @Part("visibility") visibility: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<BookmarkCreateResponse>

    @GET("bookmark/mylist")
    fun myBookmarkList(
        @Header("Authorization") authorization: String?,
    ): Call<List<MyBookmarkListResponse>>
}