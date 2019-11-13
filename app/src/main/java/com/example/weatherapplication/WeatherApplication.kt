package com.example.weatherapplication

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.example.weatherapplication.data.db.database.location.LocationProvider
import com.example.weatherapplication.data.db.database.WeatherDatabase
import com.example.weatherapplication.data.db.database.location.LocationProviderImpl
import com.example.weatherapplication.data.net.*
import com.example.weatherapplication.data.repository.WeatherRepository
import com.example.weatherapplication.data.repository.WeatherRepositoryImpl
import com.example.weatherapplication.ui.weather.current.CurrentWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.generic.bind
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton {instance<WeatherDatabase>().currentDao()}
        bind() from singleton {instance<WeatherDatabase>().locationDao()}
        bind<ConnectionInterceptor>() with singleton {ConnectionInterceptorImpl(instance())}
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetDataSource>() with singleton {WeatherNetDataSourceImpl(instance())}
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(),instance(),instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }


}