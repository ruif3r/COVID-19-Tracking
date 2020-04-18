package com.example.ncov19traking.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ncov19traking.data.NCoVDataBase
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = NCoVRepository(application)

    var nCoVAllCases = liveData(Dispatchers.IO) { emit(repo.getAllCases()) }

    var nCoVYesterdayAllCases = runBlocking(Dispatchers.IO) { repo.getAllYesterdayCases() }

    fun deleteAll() {
        runBlocking(Dispatchers.IO) {
            repo.deleteAllCases()
        }

    }

    fun getCasesPercentageDifference(universeNumber: Int, fieldNumber: Int): String {
        val difference = universeNumber.minus(fieldNumber)
        return difference.times(100).div(universeNumber.toFloat()).format(2)
    }

    fun Float.format(digits: Int) = "%.${digits}f".format(this)
}
