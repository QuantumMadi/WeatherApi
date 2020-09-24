package com.example.weatherapi.mainactivityfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.weatherapi.R
import com.example.weatherapi.adapters.CardViewAdapter
import com.example.weatherapi.models.Forecast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_forecast_info.*

class ForecastInfoFragment : Fragment(R.layout.fragment_forecast_info) {

    private lateinit var forecast: Forecast
    private val args : ForecastInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecast = args.forecast!!
        setView()
    }

    override fun onPause() {
        super.onPause()
        val view = activity?.findViewById<FloatingActionButton>(R.id.fab)
        view?.show()
    }

    private fun setView() {
        val view = activity?.findViewById<FloatingActionButton>(R.id.fab)
        view?.hide()
        forecastInfoTimeTimeTxtView.text = CardViewAdapter.getDateTime(forecast.dt!!)
        forecastInfoTimeTempTxtView.text = forecast.main?.temp.toString()
        forecastInfoTimeTempMinTxtView.text = forecast.main?.tempMin.toString()
        forecastInfoTimeTempMaxTxtView.text = forecast.main?.tempMax.toString()
        forecastInfoTimePressureTxtView.text = forecast.main?.pressure.toString()
    }
}