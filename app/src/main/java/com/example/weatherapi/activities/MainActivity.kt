package com.example.weatherapi.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapi.BuildConfig
import com.example.weatherapi.R
import com.example.weatherapi.apiservice.WeatherApi
import com.example.weatherapi.constants.PERMISSION_ID
import com.example.weatherapi.models.Forecast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        private val weatherApi by lazy {
            WeatherApi.create()
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var location: Location
    private lateinit var getForecastCallback: OnGetForecast

    interface OnGetForecast {
        fun pushNewData(body: Forecast?)
    }

    fun setOnGetForecast(getForecastCallback: OnGetForecast) {
        this.getForecastCallback = getForecastCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupListeners()
        getLocation()
    }

    private fun setupListeners() {
        fab.setOnClickListener {
            val call =
                weatherApi.getForecast(BuildConfig.API_KEY, location.latitude, location.longitude)

            call.enqueue(object : Callback<Forecast> {
                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    if (response.isSuccessful) {
                        getForecastCallback.pushNewData(response.body())
                    }
                }
                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    //region Get Location
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (isPermissionsGranted()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        this.location = location
                    } else {
                        newLocationData()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.askForGPSActivation), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            location = locationResult.lastLocation
        }
    }

    //endregion

    //region Location permissions

    private fun isPermissionsGranted(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
    //endregion
}