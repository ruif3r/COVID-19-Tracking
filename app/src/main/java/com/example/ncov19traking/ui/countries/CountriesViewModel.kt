package com.example.ncov19traking.ui.countries

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVDataBase
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers

class CountriesViewModel(context: Application) : AndroidViewModel(context) {

    private val repo = NCoVRepository(NCoVDataBase.getDataBase(context))

    var nCoVCasesByCountry = liveData(Dispatchers.IO) { emit(repo.getAllCountries()) }

    fun refreshCountryList() = nCoVCasesByCountry

    fun getErrorOnFetchFailure() = repo.notifyError()
}