package com.example.weatherapplication.data.net

import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherapplication.internal.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectionInterceptorImpl(context: Context) : ConnectionInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline())
            throw NoConnectionException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}