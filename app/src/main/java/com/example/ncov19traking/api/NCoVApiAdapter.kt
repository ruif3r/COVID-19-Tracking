package com.example.ncov19traking.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NCoVApiAdapter {

    val nCoVApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NCoVApi::class.java)
    }
}