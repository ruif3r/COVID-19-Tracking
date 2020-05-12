package com.example.ncov19traking.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.ncov19traking.api.*
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class NCoVRepository(private val context: Context) {

    private val nCoVDao = NCoVDataBase.getDataBase(context).nCoVDao()
    private val countryDao = NCoVDataBase.getDataBase(context).countryDao()
    private val globalHistoricalDao = NCoVDataBase.getDataBase(context).globalHistoricalDao()

    suspend fun getAllCases(): NCoVInfo {
        return try {
            when (val response = ApiResponse.create(NCoVApiAdapter.nCoVApi.getGeneralNumbers())) {
                is ApiSuccessResponse -> {
                    nCoVDao.save(response.data)
                    nCoVDao.load()
                }
                is ApiSuccessEmptyResponse -> nCoVDao.load()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    notifyError(response.message, response.code)
                    nCoVDao.load()
                }
            }
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
            when (val response =
                ApiResponse.create(NCoVApiAdapter.nCoVApi.getYesterdayGeneralNumbers())) {
                is ApiSuccessResponse -> {
                    nCoVDao.saveYesterday(response.data)
                    nCoVDao.loadYesterday()
                }
                is ApiSuccessEmptyResponse -> nCoVDao.loadYesterday()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    nCoVDao.loadYesterday()
                }
            }
        } catch (e: Exception) {
            if (nCoVDao.loadYesterday().size != 0)
                nCoVDao.loadYesterday().first()
            else
                NCoVInfoYesterday(0, 0, 0, 0)
        }
    }

    suspend fun getAllCountries(): Array<NumbersByCountry>? {
        return try {
            when (val response = ApiResponse.create(NCoVApiAdapter.nCoVApi.getNumbersByCountry())) {
                is ApiSuccessResponse -> {
                    countryDao.save(response.data)
                    countryDao.load()
                }
                is ApiSuccessEmptyResponse -> countryDao.load()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    notifyError(response.message, response.code)
                    countryDao.load()
                }
            }
        } catch (e: IOException) {
            Log.d("debug", "${e.cause}: ${e.message}")
            if (countryDao.load().isNotEmpty())
                countryDao.load()
            } else emptyArray()
        }
    }

    suspend fun getHistoricalCountryData() = NCoVApiAdapter.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases(): Timeline? {
        return try {
            when (val response =
                ApiResponse.create(NCoVApiAdapter.nCoVApi.getAllHistoricalData())) {
                is ApiSuccessResponse -> {
                    globalHistoricalDao.save(response.data)
                    globalHistoricalDao.load()
                }
                is ApiSuccessEmptyResponse -> globalHistoricalDao.load()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    notifyError(response.message, response.code)
                    globalHistoricalDao.load()
                }
            }
        } catch (e: Exception) {
            if (globalHistoricalDao.load().size != 0)
                globalHistoricalDao.load().first()
            else null
        }
    }

    private suspend fun notifyError(errorMessage: String?, errorCode: Int?) {
        withContext(Dispatchers.Main) {
            if (errorCode != null)
                Toast.makeText(context, "Error code $errorCode: $errorMessage", Toast.LENGTH_LONG)
                    .show()
            else
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}