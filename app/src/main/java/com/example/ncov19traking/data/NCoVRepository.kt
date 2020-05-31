package com.example.ncov19traking.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ncov19traking.api.*
import com.example.ncov19traking.daos.CountryDao
import com.example.ncov19traking.daos.GlobalHistoricalDao
import com.example.ncov19traking.daos.NCoVDao
import com.example.ncov19traking.models.*
import java.io.IOException
import javax.inject.Inject

class NCoVRepository @Inject constructor(
    private val nCoVDao: NCoVDao,
    private val countryDao: CountryDao,
    private val globalHistoricalDao: GlobalHistoricalDao,
    private var errorResponse: MutableLiveData<ErrorBody>
) {

    suspend fun getAllCases(): NCoVInfo {
        try {
            when (val response = ApiResponse.create(NCoVApiClient.nCoVApi.getGeneralNumbers())) {
                is ApiSuccessResponse -> nCoVDao.save(response.data)
                is ApiSuccessEmptyResponse -> errorResponse.postValue(ErrorBody(message = "Empty body"))
                is ApiErrorResponse -> errorResponse.postValue(
                    ErrorBody(
                        response.code,
                        response.message
                    )
                )
            }
        } catch (e: IOException) {
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (nCoVDao.load().isEmpty())
                return NCoVInfo()
        }
        return nCoVDao.load().first()
    }

    fun deleteAllCases() {
        nCoVDao.delete()
    }

    suspend fun getAllYesterdayCases(): NCoVInfoYesterday {
        try {
            when (val response =
                ApiResponse.create(NCoVApiClient.nCoVApi.getYesterdayGeneralNumbers())) {
                is ApiSuccessResponse -> nCoVDao.saveYesterday(response.data)
                is ApiSuccessEmptyResponse -> errorResponse
                is ApiErrorResponse -> Log.d("apiError", "${response.code}: ${response.message}")
            }
        } catch (e: Exception) {
            if (nCoVDao.loadYesterday().isEmpty())
                return NCoVInfoYesterday()
        }
        return nCoVDao.loadYesterday().first()
    }

    suspend fun getAllCountries(): Array<NumbersByCountry>? {
        try {
            when (val response = ApiResponse.create(NCoVApiClient.nCoVApi.getNumbersByCountry())) {
                is ApiSuccessResponse -> countryDao.save(response.data)
                is ApiSuccessEmptyResponse -> errorResponse.postValue(ErrorBody(message = "Empty body"))
                is ApiErrorResponse -> errorResponse.postValue(
                    ErrorBody(
                        response.code,
                        response.message
                    )
                )
            }
        } catch (e: IOException) {
            Log.d("debug", "${e.cause}: ${e.message}")
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (countryDao.load().isEmpty())
                return emptyArray()
        }
        return countryDao.load()
    }

    suspend fun getHistoricalCountryData() = NCoVApiClient.nCoVApi.getHistoricalDataByCountry()

    suspend fun getAllHistoricalDataCases(): Timeline? {
        try {
            when (val response =
                ApiResponse.create(NCoVApiClient.nCoVApi.getAllHistoricalData())) {
                is ApiSuccessResponse -> globalHistoricalDao.save(response.data)
                is ApiSuccessEmptyResponse -> errorResponse.postValue(ErrorBody(message = "Empty body"))
                is ApiErrorResponse -> errorResponse.postValue(
                    ErrorBody(
                        response.code,
                        response.message
                    )
                )
            }
        } catch (e: Exception) {
            errorResponse.postValue(e.message?.let { ErrorBody(message = it) })
            if (globalHistoricalDao.load().isEmpty())
                return null
        }
        return globalHistoricalDao.load().first()
    }

    fun notifyError(): LiveData<ErrorBody> {
        return errorResponse
    }
}