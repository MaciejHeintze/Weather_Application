package com.example.weatherapplication.data.net

import androidx.lifecycle.LiveData
import com.example.weatherapplication.data.net.response.CurrentWeatherResponse

interface WeatherNetDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}