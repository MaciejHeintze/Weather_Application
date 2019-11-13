package com.example.weatherapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapplication.data.db.database.current.Current
import com.example.weatherapplication.data.db.database.current.CurrentDao
import com.example.weatherapplication.data.db.database.location.LocationProvider
import com.example.weatherapplication.data.db.database.location.WeatherLocation
import com.example.weatherapplication.data.db.database.location.WeatherLocationDao
import com.example.weatherapplication.data.net.WeatherNetDataSource
import com.example.weatherapplication.data.net.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class WeatherRepositoryImpl(
    private val currentDao: CurrentDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetDataSource: WeatherNetDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    init {
        weatherNetDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistCurrent(newCurrentWeather)
        }
    }
    
    override suspend fun getCurrentWeather(): LiveData<Current> {
        return withContext(Dispatchers.IO) {
            initData()
            return@withContext currentDao.getCurrentWeatherFromDb()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistCurrent(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentDao.upsert(fetchedWeather.current)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initData(){
        val lastWeatherLocation= weatherLocationDao.getLocation().value

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather()
            return
        }
        if(isFetchNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetDataSource.fetchCurrentWeather(
            locationProvider.getLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchNeeded(lastFetchTime: ZonedDateTime):Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}