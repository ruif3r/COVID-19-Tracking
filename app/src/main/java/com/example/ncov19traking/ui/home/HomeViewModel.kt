package com.example.ncov19traking.ui.home

import androidx.lifecycle.ViewModel
import com.example.ncov19traking.data.NCoVRepository

class HomeViewModel : ViewModel() {

    private val repo = NCoVRepository()

   var nCoVAllCases  = repo.getAllCases()

    fun refreshAllCases() = nCoVAllCases
}