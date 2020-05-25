package com.example.ncov19traking.di

import android.content.Context
import androidx.room.Room
import com.example.ncov19traking.data.NCoVDataBase
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
}