package com.example.ncov19traking.ui.graphs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.BaseApp
import kotlinx.coroutines.Dispatchers

class GraphsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = getApplication<BaseApp>().applicationComponent.repository()

    val nCoVHistoricalDataByCountry = liveData(Dispatchers.IO) {emit(repo.getHistoricalCountryData())}

    val nCoVAllHistoricalData = liveData(Dispatchers.IO) {emit(repo.getAllHistoricalDataCases())}

    fun getErrorOnFetchFailure() = repo.notifyError()
}