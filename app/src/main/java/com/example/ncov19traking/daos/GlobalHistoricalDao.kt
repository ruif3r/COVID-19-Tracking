package com.example.ncov19traking.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.ncov19traking.models.Timeline

@Dao
interface GlobalHistoricalDao {

    @Insert(onConflict = REPLACE)
    fun save(timeline: Timeline)

    @Query("SELECT * FROM Timeline")
    fun load(): Array<Timeline>
}