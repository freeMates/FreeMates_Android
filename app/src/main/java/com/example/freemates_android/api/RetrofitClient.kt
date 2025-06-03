package com.example.freemates_android.api

import com.example.freemates_android.api.service.AuthService
import com.example.freemates_android.api.service.MailService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://3.34.78.124:8087/api/"


    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
    .addInterceptor(interceptor)
//            .addInterceptor { chain ->
//                val original = chain.request()
//                val requestBuilder = original.newBuilder()                      // 리퀘스트 빌더 만들고
//                    .header("Authorization", "KakaoAK ${BuildConfig.KAKAO_API_KEY}")    // 거기에 헤더 추가
//                val request = requestBuilder.build()
//                chain.proceed(request)
//            }
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)             //타임아웃 조건 추가
    .addNetworkInterceptor(interceptor)                     //로그용 okhttp 달아주고
    .build()
}

private val gson: Gson by lazy {
    GsonBuilder()
        .setLenient()
        .create()
}


private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)                              // baseurl 달고
    .addConverterFactory(GsonConverterFactory.create(gson))     // json을 data class로 편하게 바꿔주는 gson
    .client(createOkHttpClient())               // okhttp 클라이언트 달고
    .build()

    val authService: AuthService = retrofit.create(AuthService::class.java)

    val mailService: MailService = retrofit.create(MailService::class.java)
}