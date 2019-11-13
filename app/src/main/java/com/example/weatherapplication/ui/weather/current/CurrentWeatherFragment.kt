package com.example.weatherapplication.ui.weather.current

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.example.weatherapplication.R
import com.example.weatherapplication.data.net.ConnectionInterceptorImpl
import com.example.weatherapplication.data.net.WeatherApiService
import com.example.weatherapplication.data.net.WeatherNetDataSource
import com.example.weatherapplication.data.net.WeatherNetDataSourceImpl
import com.example.weatherapplication.internal.GlideApp
import com.example.weatherapplication.ui.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if(location == null) return@Observer
            setLocation(location.name)
        })
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            setTemperature(it.temparature, it.feelslike)
            setDescription(it.weatherDescription[0])
            setDetails(it.pressure, it.uvIndex, it.windSpeed)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(weather_image)
            weather_image.setBackgroundColor(Color.parseColor("#ffffff"))
            weather_image.invalidate()
        })

    }
    private fun setLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun setTemperature(temperature: Int, feels: Double){
        temparature.text = "$temperature°C"
        feels_like_value_text_view.text = "$feels °C"
    }
    private fun setDescription(description: String){
        weather_description.text = description
    }

    private fun setDetails(pressure: Double, uvIndex: Double, windSpeed: Double){
        pressure_text_view.text = "Pressure: ${pressure} hPa"
        uv_text_view.text = "UV index: ${uvIndex}"
        wind_speed_text_view.text = "Wind speed: ${windSpeed} kph"
    }

}
