package com.example.weatherapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat

const val MY_LOCATION_COARSE_PERMISSION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    override val kodein by closestKodein()
    private lateinit var navigationController: NavController

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigationController = Navigation.findNavController(this,R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navigationController)
        NavigationUI.setupActionBarWithNavController(this, navigationController)
        requestPermissions()

        if(hasLocationPermission()){
            bindLocation()
        }else
            requestPermissions()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null, navigationController)
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), MY_LOCATION_COARSE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == MY_LOCATION_COARSE_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocation()
            else
                Toast.makeText(this, "Set location in location card", Toast.LENGTH_LONG).show()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun bindLocation(){
        BoundLocationManager(this, fusedLocationProviderClient,locationCallback)
    }
}
