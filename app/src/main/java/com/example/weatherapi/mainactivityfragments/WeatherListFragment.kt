package com.example.weatherapi.mainactivityfragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.R
import com.example.weatherapi.activities.MainActivity
import com.example.weatherapi.adapters.CardViewAdapter
import com.example.weatherapi.constants.FORECAST_HISTORY
import com.example.weatherapi.models.Forecast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_weather_list.*

class WeatherListFragment : Fragment(R.layout.fragment_weather_list),
    CardViewAdapter.OnArrowClickListener, MainActivity.OnGetForecast {

    private var recyclerView: RecyclerView? = null
    private val arrowClickCallback = this
    private var forecastsHistory: MutableList<Forecast>? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getForecasts()
        setCallback()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCardViewAdapter()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun showForecastInfo(forecast: Forecast) {
        val action = WeatherListFragmentDirections.actionShowForecastDescriptionFrag(forecast)
        findNavController().navigate(action)
    }

    override fun pushNewData(body: Forecast?) {
        if (forecastsHistory != null) {
            val insertIndex = 0
            forecastsHistory?.add(insertIndex, body!!)
            recyclerView?.adapter?.notifyItemInserted(insertIndex)

            disposable = saveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(
                        context,
                        getString(R.string.savedDataCaution),
                        Toast.LENGTH_LONG
                    ).show()
                }, { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show() })

        } else {
            forecastsHistory = mutableListOf(body!!)
            setCardViewAdapter()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveData(): Completable {
        val prefs = context?.getSharedPreferences(
            getString(R.string.sharedPreferences),
            Context.MODE_PRIVATE
        )
        val json = Gson()
        val data = json.toJson(forecastsHistory)
        val editor = prefs?.edit()
        editor?.putString(FORECAST_HISTORY, data)
        editor?.apply()
        return Completable.complete()
    }

    private fun setCardViewAdapter() {
        if (forecastsHistory == null) {
            weatherListNoDataTxtView.isVisible = true
        } else {
            weatherListNoDataTxtView.isVisible = false
            recyclerView = weatherListRecyclerView.apply {
                adapter = CardViewAdapter(
                    forecastsHistory!!, arrowClickCallback
                )
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val view = activity?.findViewById<FloatingActionButton>(R.id.fab)
                        when {
                            dy < 0 -> view?.show()
                            dy == 0 -> view?.show()
                            else -> view?.hide()
                        }
                    }
                })
            }
        }
    }

    private fun setCallback() {
        val activity = activity as MainActivity
        activity.setOnGetForecast(this)
    }

    private fun getForecasts() {
        val prefs = context?.getSharedPreferences(
            getString(R.string.sharedPreferences),
            Context.MODE_PRIVATE
        )
        if (prefs != null) {
            val gson = Gson()
            val data = prefs.getString(FORECAST_HISTORY, null)
            val type = object : TypeToken<List<Forecast>>() {}.type
            forecastsHistory = gson.fromJson(data, type)
        }
    }
}