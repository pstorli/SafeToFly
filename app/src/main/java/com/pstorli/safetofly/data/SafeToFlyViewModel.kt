package com.pstorli.safetofly.data

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jankapotamus.darkskyandroidwrapper.data.Forecast
import com.pstorli.safetofly.MainActivity
import com.pstorli.safetofly.networky.SafeToFlyRepository
import com.pstorli.safetofly.util.Help
import com.pstorli.safetofly.util.Status
// import java.time.LocalDateTime
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

    val CLOUD_CEILING_DEF   = 1000
    val CLOUD_COVER_DEF     = .5
    val CONDITIONS_DEF      = "Sunny"
    val GUSTS_DEF           = 0
    val PRECIPITATION_DEF   = .1
    val TEMP_DEF            = 70
    val VISIBILITY_DEF      = 5
    val WIND_DIR            = 0
    val WIND_SPEED          = 1

    // Vars dealing with the actual values from DarkSky web service.
    // TODO Note we are using Date instead of LocalDateTime due to min sdk at 19 intead of 26
    var cloudCeiling        = CLOUD_CEILING_DEF
    var cloudCover          = CLOUD_COVER_DEF
    var conditions          = CONDITIONS_DEF
    var gusts               = GUSTS_DEF
    var precipitation       = PRECIPITATION_DEF
    var sunrise             = Date() // LocalDateTime.now()
    var sunset              = Date () //LocalDateTime.now()
    var temp                = TEMP_DEF
    var timeOfDay           = Date() // LocalDateTime.now()
    var visibility          = VISIBILITY_DEF
    var windDir             = WIND_DIR
    var windSpeed           = WIND_SPEED

    init {
        inst = this

        Log.d (appName, "SafeToFlyViewModel created")

        // Tel the rep that we want to be notificed when the forcast changes.
        safeToFlyRepository.addObserver(this)
    }

    // Extension function created to making switching between Data and LocalDateTime easier
    fun Date.hour(): Int{
        return hours
    }

    /**
     * Instance of SafeToFlyViewModel
     */
    companion object {
        lateinit var inst: SafeToFlyViewModel

        val instance: SafeToFlyViewModel
            get() {
                return inst
            }
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
                sunrise         = Help.inst.toDate ( 1000L * forecast.dailyForecast.dailyConditionsList[0].sunriseTime)
                sunset          = Help.inst.toDate ( 1000L * forecast.dailyForecast.dailyConditionsList[0].sunsetTime)
            }

            temp                = forecast.currentConditions.temperature.toInt()
            timeOfDay           = Help.inst.toDate (1000L * forecast.currentConditions.time)
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

        // Let expresso continue.
        MainActivity.instance.idlingRes.setIdle (true)
    }

    override fun onCleared() {
        super.onCleared()

        Log.d (appName, "SafeToFlyViewModel onCleared")
    }

    /**
     * Compute the overall status from adding up each individual status and dividing by the number of status.
     */
    fun computeOverallStatus ()
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
    fun updateVisibility ()
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
    fun updateCloudCeiling ()
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
    fun updateTimeOfDay ()
    {
        if (timeOfDay.hour() > sunrise.hour() && sunset.hour() - timeOfDay.hour() > 1) {
            timeOfDayStatus = Status.GREEN
        }
        else if ((sunset.hour() - timeOfDay.hour() < 1) || (sunrise.hour() - timeOfDay.hour() <= 30) || (sunset.hour() - timeOfDay.hour() <= 30)) {
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
    fun updateWindSpeed ()
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
    fun updateTemperature ()
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
    fun updatePrecipitation ()
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
