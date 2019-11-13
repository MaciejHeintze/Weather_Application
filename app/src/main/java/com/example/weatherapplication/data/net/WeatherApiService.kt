package com.example.weatherapplication.data.net

import com.example.weatherapplication.data.net.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "07bf879d13887d8887b6a8e2603dcc24"

interface WeatherApiService {

    @GET(value="current")
    fun getCurrentWeather(
        @Query(value = "query") location:String,
        @Query(value = "lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse> //kotlin corountines - await for response

    companion object{
        operator fun invoke(
            connectionInterceptor: ConnectionInterceptor
        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key",
                        API_KEY
                    )
                    .build()
                val request = chain.request().newBuilder().url(url).build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}