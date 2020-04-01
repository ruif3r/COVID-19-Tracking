package com.example.ncov19traking.data
import com.example.ncov19traking.api.NCoVApiAdapter
import com.example.ncov19traking.models.CountryHistoricalData
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline

class NCoVRepository {

    fun getAllCases() = NetworkCall<NCoVInfo>().makeCall(NCoVApiAdapter.nCoVApi.getGeneralNumbers())

    fun getAllCountries() = NetworkCall<ArrayList<NumbersByCountry>>().makeCall(NCoVApiAdapter.nCoVApi.getNumbersByCountry())

    fun getHistoricalCountryData() = NetworkCall<ArrayList<CountryHistoricalData>>().makeCall(NCoVApiAdapter.nCoVApi.getHistoricalDataByCountry())

    fun getAllHistoricalDataCases() = NetworkCall<Timeline>().makeCall(NCoVApiAdapter.nCoVApi.getAllHistoricalData())
}