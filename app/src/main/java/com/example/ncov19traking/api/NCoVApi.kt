package com.example.ncov19traking.api

import com.example.ncov19traking.models.CountryHistoricalData
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import retrofit2.Call
import retrofit2.http.GET

interface NCoVApi {

    @GET("all")
    fun getGeneralNumbers() : Call<NCoVInfo>

    @GET("countries")
    fun getNumbersByCountry() : Call<ArrayList<NumbersByCountry>>

    @GET("v2/historical")
    fun getHistoricalDataByCountry() : Call<ArrayList<CountryHistoricalData>>

    @GET("v2/historical/all")
    fun getAllHistoricalData() : Call<Timeline>
}