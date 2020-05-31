package com.example.ncov19traking.di

import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}