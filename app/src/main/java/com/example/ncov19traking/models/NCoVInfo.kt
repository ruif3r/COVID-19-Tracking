package com.example.ncov19traking.models

data class NCoVInfo(var cases: Int, var deaths: Int, var recovered: Int, var updated: Long)

data class NumbersByCountry(
    var country: String,
    var cases: Int, var todayCases: Int, var deaths: Int, var todayDeaths: Int,
    var recovered: Int, var active: Int, var critical: Int, var countryInfo : CountryInfo
)

data class CountryHistoricalData(var country: String, var timeline: Timeline)

data class Timeline(
    var cases: LinkedHashMap<String, Int>,
    var deaths: LinkedHashMap<String, Int>,
    var recovered: LinkedHashMap<String, Int>
)

data class CountryInfo(var flag : String)