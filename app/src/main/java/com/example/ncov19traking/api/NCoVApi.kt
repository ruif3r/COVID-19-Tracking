package com.example.ncov19traking.api

import com.example.ncov19traking.models.CountryHistoricalData
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import retrofit2.http.GET

interface NCoVApi {

    @GET("all")
    suspend fun getGeneralNumbers() : NCoVInfo

    @GET("countries")
    suspend fun getNumbersByCountry() : ArrayList<NumbersByCountry>

    @GET("v2/historical")
    suspend fun getHistoricalDataByCountry() : ArrayList<CountryHistoricalData>

    @GET("v2/historical/all")
    suspend fun getAllHistoricalData() : Timeline

    @GET("yesterday/all")
    suspend fun getYesterdayGeneralNumbers() : NCoVInfo
}