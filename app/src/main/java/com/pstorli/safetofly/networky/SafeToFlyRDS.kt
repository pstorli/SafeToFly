package com.pstorli.safetofly.networky

import android.util.Log
import com.jankapotamus.darkskyandroidwrapper.DarkSkyService
import com.jankapotamus.darkskyandroidwrapper.data.Forecast
import com.pstorli.safetofly.BuildConfig
import com.pstorli.safetofly.MainActivity
import com.pstorli.safetofly.data.SafeToFlyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SafeToFlyRDS (callback: Callback<Forecast>) {

    // Dark Sky Sections:
    // currently, minutely, hourly, daily, alerts, flags
    val flags = HashMap<String, String>()
    var cb : Callback<Forecast>

    init {
        // Remember where we came from
        cb = callback

        // Exclude all except 'currently' and 'daily' block from the response
        flags.put(
            Forecast.Exclusion.HTTP_QUERY_KEY,
            //Forecast.Exclusion.CURRENTLY.toString()  + "," +
            Forecast.Exclusion.MINUTELY.toString()     + "," +
            Forecast.Exclusion.HOURLY.toString()       + "," +
            //Forecast.Exclusion.DAILY.toString()      + "," +
            Forecast.Exclusion.ALERTS.toString()       + "," +
            Forecast.Exclusion.FLAGS.toString()
        )
    }

    /**
    * Get data from DarkSky
    */
    fun getData ()
    {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.darksky.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val darkSkyService: DarkSkyService =
                retrofit.create<DarkSkyService>(DarkSkyService::class.java)

            val key = BuildConfig.DARKSKY_API_KEY
            val lat = SafeToFlyViewModel.instance.gpsLoc.latitude
            val lon = SafeToFlyViewModel.instance.gpsLoc.longitude

            val forecastCall: Call<Forecast> = darkSkyService.getForecast(key, lat, lon, flags)
            forecastCall.enqueue(cb)
        }
        catch (ex: Exception) {
            Log.e(SafeToFlyViewModel.instance.appName, ex.toString())

            MainActivity.instance.showSnackBar(ex.message?:"Error fetching data from Dark Sky")
        }
    }
}