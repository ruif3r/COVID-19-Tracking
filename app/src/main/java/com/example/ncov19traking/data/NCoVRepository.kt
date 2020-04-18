package com.example.ncov19traking.data

import android.content.Context
import com.example.ncov19traking.api.NCoVApiAdapter
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline

class NCoVRepository(context: Context) {

    private val nCoVDao = NCoVDataBase.getDataBase(context).nCoVDao()
    private val countryDao = NCoVDataBase.getDataBase(context).countryDao()
    private val globalHistoricalDao = NCoVDataBase.getDataBase(context).globalHistoricalDao()

    suspend fun getAllCases(): NCoVInfo {
        return try {
            refreshAllCases()
            nCoVDao.load()
        } catch (e: Exception) {
            nCoVDao.load()
        }
    }

    private suspend fun refreshAllCases() {
        nCoVDao.save(NCoVApiAdapter.nCoVApi.getGeneralNumbers())
    }

    fun deleteAllCases() {
        nCoVDao.delete()
    }

    suspend fun getAllYesterdayCases(): NCoVInfoYesterday {
        return try {
            refreshAllYesterdayCases()
            nCoVDao.loadYesterday()
        } catch (e: Exception) {
            nCoVDao.loadYesterday()
        }
    }

    private suspend fun refreshAllYesterdayCases() {
        nCoVDao.saveYesterday(NCoVApiAdapter.nCoVApi.getYesterdayGeneralNumbers())
    }

    suspend fun getAllCountries() : Array<NumbersByCountry>{
        return try {
            refreshAllCountries()
            countryDao.load()
        } catch (e: Exception){
            countryDao.load()
        }
    }

    suspend fun refreshAllCountries(){
        countryDao.save(NCoVApiAdapter.nCoVApi.getNumbersByCountry())
    }

    suspend fun getHistoricalCountryData() = NCoVApiAdapter.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases() : Timeline {
        return try {
            refreshAllHistoricalDataCases()
            globalHistoricalDao.load()
        }catch (e : Exception){
            globalHistoricalDao.load()
        }
    }

    suspend fun refreshAllHistoricalDataCases(){
        globalHistoricalDao.save(NCoVApiAdapter.nCoVApi.getAllHistoricalData())
    }
}