package com.example.ncov19traking.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.ncov19traking.models.NumbersByCountry

@Dao
interface CountryDao {
    @Insert(onConflict = REPLACE)
    fun save(numbersByCountry : Array<NumbersByCountry>)

    @Query("SELECT * FROM NumbersByCountry")
    fun load(): Array<NumbersByCountry>

    @Query("DELETE FROM NumbersByCountry")
    fun delete()

    @Query("SELECT COUNT(*) FROM NumbersByCountry")
    fun getCountryCount(): Int
}