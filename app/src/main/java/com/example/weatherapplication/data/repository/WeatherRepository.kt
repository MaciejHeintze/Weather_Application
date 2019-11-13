package com.example.weatherapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapplication.data.db.database.current.Current
import com.example.weatherapplication.data.db.database.location.WeatherLocation

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<Current>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}