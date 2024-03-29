package com.example.ncov19traking.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NCoVInfo(
    var cases: Int = 0,
    var deaths: Int = 0,
    var recovered: Int = 0,
    var updated: Long = 0,
    @PrimaryKey @ColumnInfo(defaultValue = "current") val from: String = "current"
)

@Entity
data class NCoVInfoYesterday(
    var cases: Int = 0,
    var deaths: Int = 0,
    var recovered: Int = 0,
    var updated: Long = 0,
    @PrimaryKey @ColumnInfo(defaultValue = "yesterday") val from: String = "yesterday"
)

@Entity
data class NumbersByCountry(
    @PrimaryKey var country: String,
    var cases: Int, var todayCases: Int, var deaths: Int, var todayDeaths: Int,
    var recovered: Int, var active: Int, var critical: Int, @Embedded var countryInfo: CountryInfo
)

data class CountryHistoricalData(var country: String, var timeline: Timeline)

@Entity
data class Timeline(
    @PrimaryKey @ColumnInfo(defaultValue = "global") val from: String,
    var cases: LinkedHashMap<String, Int>,
    var deaths: LinkedHashMap<String, Int>,
    var recovered: LinkedHashMap<String, Int>
)

data class CountryInfo(var flag: String)

data class ErrorBody(var code: Int = 0, var message: String = "Unknown")