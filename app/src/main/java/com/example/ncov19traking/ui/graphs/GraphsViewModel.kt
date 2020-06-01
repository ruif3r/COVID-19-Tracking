package com.example.ncov19traking.ui.graphs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class GraphsViewModel(val repo: NCoVRepository) : ViewModel() {

    val nCoVHistoricalDataByCountry =
        liveData(Dispatchers.IO) { emit(repo.getHistoricalCountryData()) }

    val nCoVAllHistoricalData = liveData(Dispatchers.IO) { emit(repo.getAllHistoricalDataCases()) }

    fun getErrorOnFetchFailure() = repo.notifyError()
}