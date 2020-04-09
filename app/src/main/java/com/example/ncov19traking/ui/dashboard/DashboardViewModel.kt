package com.example.ncov19traking.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository

class DashboardViewModel : ViewModel() {

   private val repo = NCoVRepository()

    var nCoVCasesByCountry = liveData { emit(repo.getAllCountries()) }

    fun refreshCountryList() = nCoVCasesByCountry
}