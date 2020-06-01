package com.example.ncov19traking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.data.NCoVRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(val repo: NCoVRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NCoVRepository::class.java).newInstance(repo)
    }
}