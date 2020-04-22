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
            if (nCoVDao.getCount() != 0)
                nCoVDao.load()
            else NCoVInfo(0, 0, 0, 0)
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
            if (nCoVDao.getCount() != 0)
                nCoVDao.loadYesterday()
            else
                NCoVInfoYesterday(0, 0, 0, 0)
        }
    }

    private suspend fun refreshAllYesterdayCases() {
        nCoVDao.saveYesterday(NCoVApiAdapter.nCoVApi.getYesterdayGeneralNumbers())
    }

    suspend fun getAllCountries(): Array<NumbersByCountry>? {
        return try {
            refreshAllCountries()
            countryDao.load()
        } catch (e: Exception){
            if (countryDao.getCountryCount() != 0)
                countryDao.load()
            else emptyArray()
        }
    }

    private suspend fun refreshAllCountries() {
        countryDao.save(NCoVApiAdapter.nCoVApi.getNumbersByCountry())
    }

    suspend fun getHistoricalCountryData() = NCoVApiAdapter.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases(): Timeline? {
        return try {
            refreshAllHistoricalDataCases()
            globalHistoricalDao.load()
        }catch (e : Exception){
            if (globalHistoricalDao.getTimelineCount() != 0)
                globalHistoricalDao.load()
            else null
        }
    }

    private suspend fun refreshAllHistoricalDataCases() {
        globalHistoricalDao.save(NCoVApiAdapter.nCoVApi.getAllHistoricalData())
    }
}