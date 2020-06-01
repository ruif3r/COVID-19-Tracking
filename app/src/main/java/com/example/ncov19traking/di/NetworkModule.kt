package com.example.ncov19traking.di

import com.example.ncov19traking.api.NCoVApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitBuilder(httpInterceptor: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://corona.lmao.ninja/")
        .client(httpInterceptor)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpInterceptor() = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        response
    }.build()

    @Provides
    @Singleton
    fun providesNCoVApi(retrofit: Retrofit) = retrofit.create(NCoVApi::class.java)
}