package com.example.ncov19traking.ui.graphs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class GraphsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = NCoVRepository(application)

    val nCoVHistoricalDataByCountry = liveData(Dispatchers.IO) {emit(repo.getHistoricalCountryData())}

    val nCoVAllHistoricalData = liveData(Dispatchers.IO) {emit(repo.getAllHistoricalDataCases())}
}