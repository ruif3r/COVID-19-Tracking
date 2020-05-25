package com.example.ncov19traking.di

import android.content.Context
import com.example.ncov19traking.data.NCoVRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataBaseModule::class])
interface ApplicationComponent {

    fun repository(): NCoVRepository

    @Component.Factory
    interface factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

}
