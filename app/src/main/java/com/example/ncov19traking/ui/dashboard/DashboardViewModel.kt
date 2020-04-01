package com.example.ncov19traking.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.ncov19traking.data.NCoVRepository

class DashboardViewModel : ViewModel() {

   private val repo = NCoVRepository()

    var nCoVCasesByCountry = repo.getAllCountries()

    fun refreshCountryList() = nCoVCasesByCountry
}