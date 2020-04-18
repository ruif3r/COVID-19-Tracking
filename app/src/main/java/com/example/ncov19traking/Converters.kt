package com.example.ncov19traking

import androidx.room.TypeConverter
import com.example.ncov19traking.models.NumbersByCountry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters  {

    @TypeConverter
    fun listToJson(value : Array<NumbersByCountry>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value : String) = Gson().fromJson(value, Array<NumbersByCountry>::class.java)

    @TypeConverter
    fun mapToJson(value : LinkedHashMap<String, Int>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMap(value : String) : LinkedHashMap<String, Int> {
        val mapType = object : TypeToken<LinkedHashMap<String, Int>>(){}.type
        return Gson().fromJson(value, mapType)
    }
}