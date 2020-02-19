package com.pstorli.safetofly.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pstorli.safetofly.util.Status
// import org.threeten.bp.Date - This is having issues for ver 15, using Date instead
import java.util.*

class SafeToFlyViewModel : ViewModel() {


    // Vars that hold the computed statsu that the SafeToFlyFragment watches.
    val STATUS_COUNT        = 6
    var overallStatus       = MutableLiveData<Status>()
    var visibilityStatus    = Status.GREEN
    var cloudCeilingStatus  = Status.GREEN
    var timeOfDayStatus     = Status.GREEN
    var windSpeedStatus     = Status.GREEN
    var tempStatus          = Status.GREEN
    var precipitationStatus = Status.GREEN

    // Vars dealing with the actual values from DarkSky web service.
    // TODO Note we are using Date instead of java's Date due to min sdk at 15 intead of 26
    // ThreeTenABP  had issues
    // https://www.threeten.org/threetenbp/apidocs/org/threeten/bp/package-summary.html
    // https://github.com/JakeWharton/ThreeTenABP
    var sunrise             = Date()
    var sunset              = Date()
    var daylight            = "0:0"
    var visibility          = 5
    var cloudCeiling        = 1000
    var cloudCover          = .5
    var timeOfDay           = Date()
    var windSpeed           = 1
    var windDir             = 0.0
    var gusts               = 0
    var temperature         = 70
    var precipitation       = .1


    init {
        Log.d (this.toString(), "SafeToFlyViewModel created")
    }

    override fun onCleared() {
        super.onCleared()

        Log.d (this.toString(), "SafeToFlyViewModel onCleared")
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
     * value > 3 statute miles | Green
     * value = 3 statute miles | Yellow
     * value < 3 statute miles | Red
     */
    private fun updateVisibility (value: Int)
    {
        if (value > 3) {
            visibilityStatus = Status.GREEN
        }
        else if (3 == value) {
            visibilityStatus = Status.YELLOW
        }
        else { // < 3
            visibilityStatus = Status.RED
        }
    }

    /**
     * Update CloudCeiling
     *
     * value > 900 ft AGL (Above Ground Level) | Green
     * value = 900 ft AGL | Yellow
     * value < 900 ft AGL | Red
     */
    private fun updateCloudCeiling (value: Int)
    {
        if (value > 900) {
            cloudCeilingStatus = Status.GREEN
        }
        else if (900 == value) {
            cloudCeilingStatus = Status.YELLOW
        }
        else { // < 900
            cloudCeilingStatus = Status.RED
        }
    }

    /**
     * Update TimeOfDay
     *
     * value After sunrise and greater than 1 hour from sunset | Green
     * value Less than 1 hour before sunset | Yellow
     * value 30 min before sunrise to sunrise | Yellow
     * value Less than 30 minutes after sunset | Yellow
     * value 30 min after sunset and 30 minutes before sunrise | Red
     */
    @Suppress("DEPRECATION") // TODO Using date for now, need to use LocalDateTime instead when sdk min can be greater than 15
    private fun updateTimeOfDay (value: Date)
    {
        if (value.hours > sunrise.hours && sunset.hours - value.hours > 1) {
            timeOfDayStatus = Status.GREEN
        }
        else if ((sunset.hours - value.hours < 1) || (sunrise.hours - value.hours <= 30) || (sunset.hours - value.hours <= 30)) {
            timeOfDayStatus = Status.YELLOW
        }
        else { // 30 min after sunset and 30 minutes before sunrise
            timeOfDayStatus = Status.RED
        }
    }

    /**
     * Update WindSpeed
     *
     * value <= 10 mph | Green
     * value > 10 and <=20 | Yellow
     * value > 20 | Red
     */
    private fun updateWindSpeed (value: Int)
    {
        if (value <= 10) {
            windSpeedStatus = Status.GREEN
        }
        else if (value > 10 && value <= 20) {
            windSpeedStatus = Status.YELLOW
        }
        else { // > 20
            windSpeedStatus = Status.RED
        }
    }

    /**
     * Update Temperature
     *
     * value > 50 and <= 80 F | Green
     * value >= 37 and <= 50 F | Yellow
     * value > 80 and < 95 F | Yellow
     * value < 37 F or value >= 95 F | Red
     */
    private fun updateTemperature (value: Int)
    {
        if (value > 50 && value <= 80) {
            tempStatus = Status.GREEN
        }
        else if ((value >= 37 && value <= 50) || (value > 80 && value < 95)) {
            tempStatus = Status.YELLOW
        }
        else { // alue < 37 F or value >= 95
            tempStatus = Status.RED
        }
    }

    /**
     * Update Precipitation
     *
     * value <= .33 | Green
     * value > .33 and <= .66 | Yellow
     * value > .66 | Red
     */
    private fun updatePrecipitation (value: Float)
    {
        if (value <= .33) {
            precipitationStatus = Status.GREEN
        }
        else if (value > .33 && value <= .66) {
            precipitationStatus = Status.YELLOW
        }
        else { // value > .66
            precipitationStatus = Status.RED
        }
    }
}
