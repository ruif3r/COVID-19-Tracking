package com.example.ncov19traking.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.data.NCoVRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HomeViewModel : ViewModel() {

    private val repo = NCoVRepository()

    var nCoVAllCases = liveData(Dispatchers.IO) { emit(repo.getAllCases()) }

    var nCoVYesterdayAllCases = runBlocking { repo.getAllYesterdayCases() }

    fun getCasesPercentageDifference(universeNumber: Int, fieldNumber: Int): String {
        val difference = universeNumber.minus(fieldNumber)
        return difference.times(100).div(universeNumber.toFloat()).format(2)
    }

    fun Float.format(digits: Int) = "%.${digits}f".format(this)
}
