package com.example.ncov19traking.data
import com.example.ncov19traking.api.NCoVApiAdapter

class NCoVRepository {

    suspend fun getAllCases() = NCoVApiAdapter.nCoVApi.getGeneralNumbers()

    suspend fun getAllYesterdayCases() = NCoVApiAdapter.nCoVApi.getYesterdayGeneralNumbers()

    suspend fun getAllCountries() = NCoVApiAdapter.nCoVApi.getNumbersByCountry()

    suspend fun getHistoricalCountryData() = NCoVApiAdapter.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases() = NCoVApiAdapter.nCoVApi.getAllHistoricalData()
}