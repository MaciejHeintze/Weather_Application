package com.example.weatherapplication.data.net.response

import com.example.weatherapplication.data.db.database.current.Current
import com.example.weatherapplication.data.db.database.location.WeatherLocation
import com.example.weatherapplication.data.db.database.Request


data class CurrentWeatherResponse(
    val current: Current,
    val location: WeatherLocation,
    val request: Request
)