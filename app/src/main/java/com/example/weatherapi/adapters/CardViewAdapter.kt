package com.example.weatherapi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.R
import com.example.weatherapi.models.Forecast
import java.text.SimpleDateFormat
import java.util.*

class CardViewAdapter(
    private val weatherForecasts: List<Forecast>,
    private val callback: OnArrowClickListener
) :
    RecyclerView.Adapter<CardViewAdapter.ForecastViewHolder>() {

    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getDateTime(s: Long): String? {
            return try {
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val netDate = Date(s * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }
    }

    interface OnArrowClickListener {
        fun showForecastInfo(forecast: Forecast)
    }

    inner class ForecastViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.cardview_weather_item, parent, false
            )
        ) {
        private var cityName: TextView? = null
        private var date: TextView? = null
        private var temp: TextView? = null
        private var arrow: ImageView? = null

        init {
            cityName = itemView.findViewById(R.id.cardViewWeatherItemCityName)
            date = itemView.findViewById(R.id.cardViewWeatherTimeTxtView)
            temp = itemView.findViewById(R.id.cardViewWeatherItemTempTextView)
            arrow = itemView.findViewById(R.id.cardViewWeatherItemArrow)
        }

        fun bind(forecast: Forecast) {
            cityName?.text = forecast.name
            date?.text = getDateTime(forecast.dt!!)
            temp?.text = forecast.main?.temp.toString()
            setClickListener(forecast)
        }

        private fun setClickListener(forecast: Forecast) {
            arrow?.setOnClickListener {
                callback.showForecastInfo(forecast)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ForecastViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CardViewAdapter.ForecastViewHolder, position: Int) {
        val forecast: Forecast = weatherForecasts[position]
        holder.bind(forecast)
    }

    override fun getItemCount(): Int = weatherForecasts.size
}
