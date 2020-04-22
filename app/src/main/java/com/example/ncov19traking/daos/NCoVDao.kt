package com.example.ncov19traking.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.models.NCoVInfoYesterday

@Dao
interface NCoVDao {
    @Insert(onConflict = REPLACE)
    fun save(nCoVInfo: NCoVInfo)

    @Query("SELECT * FROM nCoVInfo")
    fun load(): NCoVInfo

    @Query("SELECT COUNT(*) FROM NCoVInfo")
    fun getCount(): Int

    @Query("DELETE FROM nCoVInfo")
    fun delete()

    @Insert(onConflict = REPLACE)
    fun saveYesterday(nCovInfoYesterday : NCoVInfoYesterday)

    @Query("SELECT * FROM nCovInfoYesterday")
    fun loadYesterday():NCoVInfoYesterday
}