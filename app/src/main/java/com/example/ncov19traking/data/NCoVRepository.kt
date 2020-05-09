package com.example.ncov19traking.data

import android.content.Context
import android.util.Log
import com.example.ncov19traking.api.NCoVApiAdapter
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import java.io.IOException

class NCoVRepository(private val context: Context) {

    private val nCoVDao = NCoVDataBase.getDataBase(context).nCoVDao()
    private val countryDao = NCoVDataBase.getDataBase(context).countryDao()
    private val globalHistoricalDao = NCoVDataBase.getDataBase(context).globalHistoricalDao()

    suspend fun getAllCases(): NCoVInfo {
        return try {
            refreshAllCases()
            nCoVDao.load().first()
        } catch (e: IOException) {
            if (nCoVDao.load().size != 0)
                nCoVDao.load().first()
            else NCoVInfo(0, 0, 0, 0)
        }
    }

    private suspend fun refreshAllCases() {
            NCoVApiAdapter.nCoVApi.getGeneralNumbers().body()?.let { nCoVDao.save(it) }
    }

    fun deleteAllCases() {
        nCoVDao.delete()
    }

    suspend fun getAllYesterdayCases(): NCoVInfoYesterday {
        return try {
            refreshAllYesterdayCases()
            nCoVDao.loadYesterday().first()
        } catch (e: Exception) {
            if (nCoVDao.loadYesterday().size != 0)
                nCoVDao.loadYesterday().first()
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
        } catch (e: IOException) {
            Log.d("debug", "${e.cause}: ${e.message}")
            if (countryDao.load().isNotEmpty())
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
            globalHistoricalDao.load().first()
        }catch (e : Exception){
            if (globalHistoricalDao.load().size != 0)
                globalHistoricalDao.load().first()
            else null
        }
    }

    private suspend fun refreshAllHistoricalDataCases() {
        globalHistoricalDao.save(NCoVApiAdapter.nCoVApi.getAllHistoricalData())
    }
}