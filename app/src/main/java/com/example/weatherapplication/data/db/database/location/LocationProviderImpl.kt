package com.example.weatherapplication.data.db.database.location

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapplication.internal.LocationPermissionException
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import android.Manifest
import android.annotation.SuppressLint
import com.example.weatherapplication.data.db.database.PreferenceProvider
import com.example.weatherapplication.internal.asDeferred

const val DEVICE_LOCATION = "DEVICE_LOCATION"
const val INPUT_LOCATION = "INPUT_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
    ) : PreferenceProvider(context),
    LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val locationChanged =
            try{
                hasDeviceLocationChanged(lastWeatherLocation)
            }catch (e: LocationPermissionException){
                false
            }
        return locationChanged || hasInputLocationChanged(lastWeatherLocation)
    }

    private suspend fun hasDeviceLocationChanged(lastLocation: WeatherLocation): Boolean{
        if(!isDeviceLocation())
            return false

        return true
    }

    override suspend fun getLocationString(): String {
        if(isDeviceLocation()){
            try{
                val deviceLocation = getLastDeviceLocation().await() ?: return "${getInputLocationName()}"
                return "${deviceLocation.latitude}, ${deviceLocation.longitude}"
            }catch (e: LocationPermissionException){
                return "${getInputLocationName()}"
            }
        }else
            return "${getInputLocationName()}"
    }

    private fun hasInputLocationChanged(lastLocation: WeatherLocation): Boolean{
        val input = getInputLocationName()
        return input != lastLocation.name
    }

    private fun getInputLocationName(): String?{
        return preference.getString(INPUT_LOCATION, null)
    }

    private fun isDeviceLocation(): Boolean{
        return preference.getBoolean(DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?>{
       return if(hasPermission())
           fusedLocationProviderClient.lastLocation.asDeferred()
        else
           throw LocationPermissionException()
    }

    private fun hasPermission():Boolean{
        return ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_DENIED
    }
}


