package com.pstorli.safetofly.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jankapotamus.darkskyandroidwrapper.data.Forecast
import com.pstorli.safetofly.networky.SafeToFlyRepository
import com.pstorli.safetofly.util.Status
import java.time.LocalDateTime
import java.util.*

class SafeToFlyViewModel : Observer, ViewModel() {

    // The app name. See also r.string.app_name
    val appName = "SafeToFly"

    // Our last known GPS location.
    var gpsLoc = Location ("Storli Designs LLC") // Until really initialized, who knows where we really are.

    // This repo holds the forcast. Observe safeToFlyRepository.forcast
    val safeToFlyRepository = SafeToFlyRepository ()

    // Vars that hold the computed statsu that the SafeToFlyFragment watches.
    val STATUS_COUNT        = 6
    var overallStatus       = MutableLiveData<Status>()
    var visibilityStatus    = Status.GREEN
    var cloudCeilingStatus  = Status.GREEN
    var timeOfDayStatus     = Status.GREEN
    var windSpeedStatus     = Status.GREEN
    var tempStatus          = Status.GREEN
    var precipitationStatus = Status.GREEN

    /**
     * Instance of SafeToFlyViewModel
     */
    companion object {
        private lateinit var inst: SafeToFlyViewModel

        val instance: SafeToFlyViewModel
            get() {
                return inst
            }
    }

    // Vars dealing with the actual values from DarkSky web service.
    // TODO Note we are using Date instead of java's Date due to min sdk at 15 intead of 26
    // ThreeTenABP  had issues
    // https://www.threeten.org/threetenbp/apidocs/org/threeten/bp/package-summary.html
    // https://github.com/JakeWharton/ThreeTenABP
    //
    // Work around issue that Date needs min sdk 26, and I'm on 15
    // implementation 'com.jakewharton.threetenabp:threetenabp:1.2.2'

    var cloudCeiling        = 1000
    var cloudCover          = .5
    var conditions          = "Sunny"
    var gusts               = 0
    var precipitation       = .1
    var sunrise             = Date()
    var sunset              = Date()
    var temp                = 70
    var timeOfDay           = Date()
    var visibility          = 5
    var windDir             = 0
    var windSpeed           = 1


    init {
        inst = this

        Log.d (appName, "SafeToFlyViewModel created")

        // Tel the rep that we want to be notificed when the forcast changes.
        safeToFlyRepository.addObserver(this)
    }

    /**
     * Get data from DarkSky
     */
    fun getData ()
    {
        // Tell the repository to get some data.
        safeToFlyRepository.getData()
    }


    /**
     * This gets called when the forcast changes by calling getData() above
     */
    override fun update(o: Observable?, forecast: Any?) {
        // Got forecast?
        if (forecast is Forecast) {

            cloudCover          = forecast.currentConditions.cloudCover
            conditions          = forecast.currentConditions.summary
            precipitation       = forecast.currentConditions.precipProbability

            // TODO Instead of picking item 0, go through list and find correct time.
            if (null != forecast.dailyForecast) {
                sunrise         = Date(1000L * forecast.dailyForecast.dailyConditionsList[0].sunriseTime)
                sunset          = Date(1000L * forecast.dailyForecast.dailyConditionsList[0].sunsetTime)
            }

            temp                = forecast.currentConditions.temperature.toInt()
            timeOfDay           = Date (1000L * forecast.currentConditions.time)
            visibility          = forecast.currentConditions.visibility.toInt()
            windDir             = forecast.currentConditions.windBearing
            windSpeed           = forecast.currentConditions.windSpeed.toInt()

            // /////////////////////////////////////////////////////////////////////////////////////
            // Calculate these.
            // /////////////////////////////////////////////////////////////////////////////////////

            // TODO (temp - dew point) / 4.4 = feet above sea level Check this for accuracy
            cloudCeiling        = ((temp - forecast.currentConditions.dewPoint) / 4.4).toInt()

            // TODO Currently not getting windGusts data, so fake it.
            gusts               = ((1..10).random().toDouble() * forecast.currentConditions.windSpeed).toInt() // forecast.currentConditions.windGust

        }

        // Now call member function to update their status.
        updateVisibility ()
        updateCloudCeiling()
        updateTimeOfDay()
        updateWindSpeed()
        updateTemperature()
        updatePrecipitation()

        // Lastly compute the overall status and notfiy the SafeToFlyFragment to update the UI
        computeOverallStatus ()
    }

    override fun onCleared() {
        super.onCleared()

        Log.d (appName, "SafeToFlyViewModel onCleared")
    }

    /**
     * Compute the overall status from adding up each individual status and dividing by the number of status.
     */
    private fun computeOverallStatus ()
    {
        val newOverallStatus = (visibilityStatus.value + cloudCeilingStatus.value + timeOfDayStatus.value + windSpeedStatus.value + tempStatus.value + precipitationStatus.value) / STATUS_COUNT
        when (newOverallStatus) {
            1 -> overallStatus.setValue (Status.GREEN)
            2 -> overallStatus.setValue (Status.YELLOW)
            3 -> overallStatus.setValue (Status.RED)
        }
    }

    /**
     * Update Visibility Status
     *
     * visibility > 3 statute miles | Green
     * visibility = 3 statute miles | Yellow
     * visibility < 3 statute miles | Red
     */
    private fun updateVisibility ()
    {
        if (visibility > 3) {
            visibilityStatus = Status.GREEN
        }
        else if (3 == visibility) {
            visibilityStatus = Status.YELLOW
        }
        else { // < 3
            visibilityStatus = Status.RED
        }
    }

    /**
     * Update CloudCeiling
     *
     * cloudCeiling > 900 ft AGL (Above Ground Level) | Green
     * cloudCeiling = 900 ft AGL | Yellow
     * cloudCeiling < 900 ft AGL | Red
     */
    private fun updateCloudCeiling ()
    {
        if (cloudCeiling > 900) {
            cloudCeilingStatus = Status.GREEN
        }
        else if (900 == cloudCeiling) {
            cloudCeilingStatus = Status.YELLOW
        }
        else { // < 900
            cloudCeilingStatus = Status.RED
        }
    }

    /**
     * Update TimeOfDay
     *
     * timeOfDay After sunrise and greater than 1 hour from sunset | Green
     * timeOfDay Less than 1 hour before sunset | Yellow
     * timeOfDay 30 min before sunrise to sunrise | Yellow
     * timeOfDay Less than 30 minutes after sunset | Yellow
     * timeOfDay 30 min after sunset and 30 minutes before sunrise | Red
     */
    @Suppress("DEPRECATION") // TODO Using date for now, need to use LocalDateTime instead when sdk min can be greater than 15
    private fun updateTimeOfDay ()
    {
        if (timeOfDay.hours > sunrise.hours && sunset.hours - timeOfDay.hours > 1) {
            timeOfDayStatus = Status.GREEN
        }
        else if ((sunset.hours - timeOfDay.hours < 1) || (sunrise.hours - timeOfDay.hours <= 30) || (sunset.hours - timeOfDay.hours <= 30)) {
            timeOfDayStatus = Status.YELLOW
        }
        else { // 30 min after sunset and 30 minutes before sunrise
            timeOfDayStatus = Status.RED
        }
    }

    /**
     * Update WindSpeed
     *
     * windSpeed <= 10 mph | Green
     * windSpeed > 10 and <=20 | Yellow
     * windSpeed > 20 | Red
     */
    private fun updateWindSpeed ()
    {
        if (windSpeed <= 10) {
            windSpeedStatus = Status.GREEN
        }
        else if (windSpeed > 10 && windSpeed <= 20) {
            windSpeedStatus = Status.YELLOW
        }
        else { // > 20
            windSpeedStatus = Status.RED
        }
    }

    /**
     * Update Temperature
     *
     * temp > 50 and <= 80 F | Green
     * temp >= 37 and <= 50 F | Yellow
     * temp > 80 and < 95 F | Yellow
     * temp < 37 F or temp >= 95 F | Red
     */
    private fun updateTemperature ()
    {
        if (temp > 50 && temp <= 80) {
            tempStatus = Status.GREEN
        }
        else if ((temp >= 37 && temp <= 50) || (temp > 80 && temp < 95)) {
            tempStatus = Status.YELLOW
        }
        else { // alue < 37 F or temp >= 95
            tempStatus = Status.RED
        }
    }

    /**
     * Update Precipitation
     *
     * precipitation <= .33 | Green
     * precipitation > .33 and <= .66 | Yellow
     * precipitation > .66 | Red
     */
    private fun updatePrecipitation ()
    {
        if (precipitation <= .33) {
            precipitationStatus = Status.GREEN
        }
        else if (precipitation > .33 && precipitation <= .66) {
            precipitationStatus = Status.YELLOW
        }
        else { // precipitation > .66
            precipitationStatus = Status.RED
        }
    }
}
