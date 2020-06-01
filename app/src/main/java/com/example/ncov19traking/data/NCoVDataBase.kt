package com.example.ncov19traking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ncov19traking.Converters
import com.example.ncov19traking.daos.CountryDao
import com.example.ncov19traking.daos.GlobalHistoricalDao
import com.example.ncov19traking.daos.NCoVDao
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline
import javax.inject.Singleton

@Database(
    entities = [NCoVInfo::class, NCoVInfoYesterday::class, NumbersByCountry::class, Timeline::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
@Singleton
abstract class NCoVDataBase : RoomDatabase() {
    abstract fun nCoVDao(): NCoVDao
    abstract fun countryDao(): CountryDao
    abstract fun globalHistoricalDao(): GlobalHistoricalDao
}
