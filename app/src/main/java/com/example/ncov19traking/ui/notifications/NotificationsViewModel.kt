package com.example.ncov19traking.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class NotificationsViewModel : ViewModel() {

    private val repo = NCoVRepository()

    val nCoVHistoricalDataByCountry = liveData(Dispatchers.IO) {emit(repo.getHistoricalCountryData())}

    val nCoVAllHistoricalData = liveData {emit(repo.getAllHistoricalDataCases())}
}