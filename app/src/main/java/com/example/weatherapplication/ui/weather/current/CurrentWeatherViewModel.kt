package com.example.weatherapplication.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.weatherapplication.data.repository.WeatherRepository
import com.example.weatherapplication.internal.lazyDeferred

class CurrentWeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    val weather by lazyDeferred { weatherRepository.getCurrentWeather() }
    val weatherLocation  by lazyDeferred{
        weatherRepository.getWeatherLocation()
    }
}
