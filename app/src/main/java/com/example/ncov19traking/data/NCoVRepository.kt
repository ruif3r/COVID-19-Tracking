package com.example.ncov19traking.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ncov19traking.api.*
import com.example.ncov19traking.models.*
import java.io.IOException

class NCoVRepository(nCoVDataBase: NCoVDataBase) {

    private val nCoVDao = nCoVDataBase.nCoVDao()
    private val countryDao = nCoVDataBase.countryDao()
    private val globalHistoricalDao = nCoVDataBase.globalHistoricalDao()
    private var errorResponse = MutableLiveData<ErrorBody>()

    suspend fun getAllCases(): NCoVInfo {
        return try {
            when (val response = ApiResponse.create(NCoVApiClient.nCoVApi.getGeneralNumbers())) {
                is ApiSuccessResponse -> {
                    nCoVDao.save(response.data)
                    nCoVDao.load().first()
                }
                is ApiSuccessEmptyResponse -> nCoVDao.load().first()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    errorResponse.postValue(ErrorBody(response.code, response.message))
                    nCoVDao.load().first()
                }
            }
        } catch (e: IOException) {
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (nCoVDao.load().isNotEmpty())
                nCoVDao.load().first()
            else NCoVInfo(0, 0, 0, 0)
        }
    }

    fun deleteAllCases() {
        nCoVDao.delete()
    }

    suspend fun getAllYesterdayCases(): NCoVInfoYesterday {
        return try {
            when (val response =
                ApiResponse.create(NCoVApiClient.nCoVApi.getYesterdayGeneralNumbers())) {
                is ApiSuccessResponse -> {
                    nCoVDao.saveYesterday(response.data)
                    nCoVDao.loadYesterday().first()
                }
                is ApiSuccessEmptyResponse -> nCoVDao.loadYesterday().first()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    nCoVDao.loadYesterday().first()
                }
            }
        } catch (e: Exception) {
            if (nCoVDao.loadYesterday().isNotEmpty())
                nCoVDao.loadYesterday().first()
            else
                NCoVInfoYesterday(0, 0, 0, 0)
        }
    }

    suspend fun getAllCountries(): Array<NumbersByCountry>? {
        return try {
            when (val response = ApiResponse.create(NCoVApiClient.nCoVApi.getNumbersByCountry())) {
                is ApiSuccessResponse -> {
                    countryDao.save(response.data)
                    countryDao.load()
                }
                is ApiSuccessEmptyResponse -> countryDao.load()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    errorResponse.postValue(ErrorBody(response.code, response.message))
                    countryDao.load()
                }
            }
        } catch (e: IOException) {
            Log.d("debug", "${e.cause}: ${e.message}")
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (countryDao.load().isNotEmpty())
                countryDao.load()
            else emptyArray()
        }
    }


    suspend fun getHistoricalCountryData() = NCoVApiClient.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases(): Timeline? {
        return try {
            when (val response =
                ApiResponse.create(NCoVApiClient.nCoVApi.getAllHistoricalData())) {
                is ApiSuccessResponse -> {
                    globalHistoricalDao.save(response.data)
                    globalHistoricalDao.load().first()
                }
                is ApiSuccessEmptyResponse -> globalHistoricalDao.load().first()
                is ApiErrorResponse -> {
                    Log.d("apiError", "${response.code}: ${response.message}")
                    errorResponse.postValue(ErrorBody(response.code, response.message))
                    globalHistoricalDao.load().first()
                }
            }
        } catch (e: Exception) {
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (globalHistoricalDao.load().isNotEmpty())
                globalHistoricalDao.load().first()
            else null
        }
    }

    fun notifyError(): LiveData<ErrorBody> {
        return errorResponse
    }
}