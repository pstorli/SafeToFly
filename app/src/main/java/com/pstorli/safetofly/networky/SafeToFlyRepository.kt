package com.pstorli.safetofly.networky

import android.util.Log
import com.jankapotamus.darkskyandroidwrapper.data.Forecast
import com.pstorli.safetofly.MainActivity
import com.pstorli.safetofly.data.SafeToFlyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SafeToFlyRepository : Observable(), Callback<Forecast> {

    var safeToFlyRDS : SafeToFlyRDS

    init {
        safeToFlyRDS = SafeToFlyRDS (this)
    }

    /**
     * Get data from DarkSky
     */
    fun getData ()
    {
        // Tell the rds to get some data.
        safeToFlyRDS.getData()
    }

    /**
     * Failure calling dark sky!
     */
    override fun onFailure(call: Call<Forecast>, t: Throwable) {
        MainActivity.instance.showSnackBar ("Error in call: " + t.getLocalizedMessage())
    }

    /**
     * Success calling dark sky!
     */
    override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
        try {
            // Update the forcast.
            val forecast = response.body()

            // Call setChanges() prior to calling notifyObservers ()
            setChanged()              // Inherited from Observable()
            notifyObservers(forecast) // Inherited from Observable()

            if (forecast != null) {
                Log.d(SafeToFlyViewModel.instance.appName, forecast.toString())
            }
        }
        catch (ex: Exception) {
            Log.e(SafeToFlyViewModel.instance.appName, ex?.localizedMessage)
        }
    }
}