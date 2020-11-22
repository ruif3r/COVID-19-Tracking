package com.example.ncov19traking.di

import android.content.Context
import com.example.ncov19traking.data.NCoVRepository
import com.example.ncov19traking.ui.countries.CountriesFragment
import com.example.ncov19traking.ui.global.GlobalFragment
import com.example.ncov19traking.ui.graphs.GraphsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataBaseModule::class, ViewModelModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun repository(): NCoVRepository

    fun inject(globalFragment: GlobalFragment)

    fun inject(countriesFragment: CountriesFragment)

    fun inject(graphsFragment: GraphsFragment)

    @Component.Factory
    interface factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
