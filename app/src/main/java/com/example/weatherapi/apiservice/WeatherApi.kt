package com.example.weatherapi.apiservice

import com.example.weatherapi.BuildConfig
import com.example.weatherapi.models.Forecast
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Call<Forecast>

    companion object {
        fun create(): WeatherApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }
}