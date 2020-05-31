package com.example.ncov19traking.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class CountriesViewModel(val repo: NCoVRepository) : ViewModel() {

    var nCoVCasesByCountry = liveData(Dispatchers.IO) { emit(repo.getAllCountries()) }

    fun refreshCountryList() = nCoVCasesByCountry

    fun getErrorOnFetchFailure() = repo.notifyError()
}