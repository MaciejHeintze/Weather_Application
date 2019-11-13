package com.example.weatherapplication.data.db.database.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapplication.data.db.database.location.LOCATION_ID
import com.example.weatherapplication.data.db.database.location.WeatherLocation

@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: WeatherLocation) //update insert

    @Query("select * from weather_location where id = $LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>
}