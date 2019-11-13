package com.example.weatherapplication.data.db.database.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(current: Current) //update  insert

    @Query("select * from current_weather where id = $CURRENT_ID")
    fun getCurrentWeatherFromDb():LiveData<Current>
}