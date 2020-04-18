package com.example.ncov19traking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ncov19traking.Converters
import com.example.ncov19traking.daos.CountryDao
import com.example.ncov19traking.daos.GlobalHistoricalDao
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday
import com.example.ncov19traking.models.NumbersByCountry
import com.example.ncov19traking.models.Timeline

@Database(
    entities = [NCoVInfo::class, NCoVInfoYesterday::class, NumbersByCountry::class, Timeline::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NCoVDataBase : RoomDatabase() {
    abstract fun nCoVDao(): NCoVDao
    abstract fun countryDao(): CountryDao
    abstract fun globalHistoricalDao(): GlobalHistoricalDao

    companion object {

        @Volatile
        private var INSTANCE: NCoVDataBase? = null

        fun getDataBase(context: Context): NCoVDataBase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NCoVDataBase::class.java,
                    "covid-19_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
