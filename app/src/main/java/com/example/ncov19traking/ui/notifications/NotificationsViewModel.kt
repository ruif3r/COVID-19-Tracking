package com.example.ncov19traking.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ncov19traking.data.NCoVRepository
import com.example.ncov19traking.models.CountryHistoricalData

class NotificationsViewModel : ViewModel() {

    private val repo = NCoVRepository()

    val nCoVHistoricalDataByCountry = repo.getHistoricalCountryData()

    val nCoVAllHistoricalData = repo.getAllHistoricalDataCases()
}