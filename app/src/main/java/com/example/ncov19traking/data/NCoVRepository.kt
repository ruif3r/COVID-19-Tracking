package com.example.ncov19traking.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.ncov19traking.api.NCoVApiAdapter
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import retrofit2.HttpException
import java.io.IOException

class NCoVRepository(private val context: Context) {

    private val nCoVDao = NCoVDataBase.getDataBase(context).nCoVDao()
    private val countryDao = NCoVDataBase.getDataBase(context).countryDao()
    private val globalHistoricalDao = NCoVDataBase.getDataBase(context).globalHistoricalDao()

    suspend fun getAllCases(): NCoVInfo {
        return try {
            refreshAllCases()
            nCoVDao.load()
        } catch (e: IOException) {
            when (e) {
                is HttpException -> Toast.makeText(
                    context,
                    "${e.code()}: ${e.message()}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (nCoVDao.getCount() != 0)
                nCoVDao.load()
            else NCoVInfo(0, 0, 0, 0)
        }
    }

    private suspend fun refreshAllCases() {
        if (NCoVApiAdapter.nCoVApi.getGeneralNumbers().isSuccessful) {
            NCoVApiAdapter.nCoVApi.getGeneralNumbers().body()?.let { nCoVDao.save(it) }
        }
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
        } catch (e: IOException) {
            Log.d("debug", "${e.cause}: ${e.message}")
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