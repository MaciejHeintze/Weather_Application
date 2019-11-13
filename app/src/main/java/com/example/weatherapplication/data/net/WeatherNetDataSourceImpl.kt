package com.example.weatherapplication.data.net

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapplication.data.net.response.CurrentWeatherResponse
import com.example.weatherapplication.internal.NoConnectionException

class WeatherNetDataSourceImpl(private val weatherApiService: WeatherApiService
    ) : WeatherNetDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get()=_downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try{
            val fetchCurrentWeather = weatherApiService
                .getCurrentWeather(location,languageCode)
                .await()
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        }
        catch(e: NoConnectionException){
            Log.e("Connection", "No internet connection",e)
        }
    }
}