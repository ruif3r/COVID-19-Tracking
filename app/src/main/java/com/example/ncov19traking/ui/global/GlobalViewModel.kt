package com.example.ncov19traking.ui.global

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.ncov19traking.BaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class GlobalViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = getApplication<BaseApp>().applicationComponent.repository()

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

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)

    fun getErrorOnFetchFailure() = repo.notifyError()
}
