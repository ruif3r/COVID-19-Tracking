package com.example.ncov19traking.ui.countries

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.BaseApp
import kotlinx.coroutines.Dispatchers

class CountriesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = getApplication<BaseApp>().applicationComponent.repository()

    var nCoVCasesByCountry = liveData(Dispatchers.IO) { emit(repo.getAllCountries()) }

    fun refreshCountryList() = nCoVCasesByCountry

    fun getErrorOnFetchFailure() = repo.notifyError()
}