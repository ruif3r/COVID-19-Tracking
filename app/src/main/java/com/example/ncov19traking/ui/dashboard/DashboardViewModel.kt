package com.example.ncov19traking.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = NCoVRepository(application)

    var nCoVCasesByCountry = liveData(Dispatchers.IO) { emit(repo.getAllCountries()) }

    fun refreshCountryList() = nCoVCasesByCountry
}