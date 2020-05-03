package com.example.ncov19traking.api

import com.example.ncov19traking.models.*
import retrofit2.Response
import retrofit2.http.GET

interface NCoVApi {

    @GET("v2/all")
    suspend fun getGeneralNumbers(): Response<NCoVInfo>

    @GET("v2/countries")
    suspend fun getNumbersByCountry(): Response<Array<NumbersByCountry>>

    @GET("v2/historical")
    suspend fun getHistoricalDataByCountry() : ArrayList<CountryHistoricalData>

    @GET("v2/historical/all")
    suspend fun getAllHistoricalData(): Response<Timeline>

    @GET("v2/all?yesterday=true")
    suspend fun getYesterdayGeneralNumbers(): Response<NCoVInfoYesterday>
}