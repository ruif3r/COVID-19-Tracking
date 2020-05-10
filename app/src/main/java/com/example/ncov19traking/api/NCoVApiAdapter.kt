package com.example.ncov19traking.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NCoVApiAdapter {

    val nCoVApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja/")
            .client(httpInterceptor)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NCoVApi::class.java)
    }

    private val httpInterceptor = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        return@addInterceptor response
    }.build()
}