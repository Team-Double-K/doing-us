package com.kim.hackathon.config

import com.kim.hackathon.utils.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun init(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.218.61:8000/api/") // 여기에 API의 기본 URL을 넣으세요.
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}