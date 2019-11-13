package com.example.weatherapplication.data.db.database.location

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getLocationString(): String
}