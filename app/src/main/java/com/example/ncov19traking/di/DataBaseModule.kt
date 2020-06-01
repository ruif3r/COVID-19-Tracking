package com.example.ncov19traking.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.ncov19traking.data.NCoVDataBase
import com.example.ncov19traking.models.ErrorBody
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context) = Room.databaseBuilder(
        context,
        NCoVDataBase::class.java,
        "covid-19_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideGlobalCasesDao(database: NCoVDataBase) = database.nCoVDao()

    @Provides
    fun provideCountryDao(database: NCoVDataBase) = database.countryDao()

    @Provides
    fun provideGlobalHistoricalDao(database: NCoVDataBase) = database.globalHistoricalDao()

    @Provides
    fun provideMutableErrorBody() = MutableLiveData<ErrorBody>()
}