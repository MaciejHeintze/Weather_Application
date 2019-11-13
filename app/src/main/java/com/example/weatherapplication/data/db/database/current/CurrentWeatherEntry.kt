package com.example.weatherapplication.data.db.database.current

interface CurrentWeatherEntry {
    val feelslike: Double
    val observationTime: String
    val pressure: Double
    val temparature: Double
    val uvIndex: Double
    val weatherIcons: List<String>
    val windSpeed: Double
    val weatherDescription: List<String>
}