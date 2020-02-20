package com.pstorli.safetofly.screens.main

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pstorli.safetofly.MainActivity
import com.pstorli.safetofly.R
import com.pstorli.safetofly.data.SafeToFlyViewModel
import com.pstorli.safetofly.util.Status
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SafeToFlyFragment () : Fragment() {
    lateinit var viewModel: SafeToFlyViewModel

    private val dateFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.US)

    // TODO Change to use new Google binding feature will eliminate these. https://developer.android.com/topic/libraries/view-binding
    // Fields with ids from safe_to_fly_fragment.xml
    private lateinit var cloudCeil:     TextView
    private lateinit var cloudCover:    TextView
    private lateinit var cloudStatus:   ImageView
    private lateinit var daylight:      TextView
    private lateinit var gusts:         TextView
    private lateinit var location:      TextView
    private lateinit var precipText:    TextView
    private lateinit var rainDrop:      ImageView
    private lateinit var remain:        TextView
    private lateinit var sunrise:       TextView
    private lateinit var sunriseText:   TextView
    private lateinit var sunset:        TextView
    private lateinit var sunsetText:    TextView
    private lateinit var temp:          TextView
    private lateinit var tempStatus:    ImageView
    private lateinit var tempText:      TextView
    private lateinit var time:          TextView
    private lateinit var todStatus:     ImageView
    private lateinit var vis:           TextView
    private lateinit var visStatus:     ImageView
    private lateinit var visText:       TextView
    private lateinit var wind:          TextView
    private lateinit var windDir:       TextView
    private lateinit var windStatus:    ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.safe_to_fly_fragment, container, false)

        // TODO Change to use new Google binding feature will eliminate these. https://developer.android.com/topic/libraries/view-binding
        // Fetch these from the inflated gui.
        cloudCeil       = view.findViewById(R.id.cloudCeil)
        cloudStatus     = view.findViewById(R.id.cloudStatus)
        cloudCover      = view.findViewById(R.id.cloudCover)
        daylight        = view.findViewById(R.id.daylight)
        gusts           = view.findViewById(R.id.gusts)
        location        = view.findViewById(R.id.location)
        precipText      = view.findViewById(R.id.precipText)
        rainDrop        = view.findViewById(R.id.rainDrop)
        remain          = view.findViewById(R.id.remain)
        sunrise         = view.findViewById(R.id.sunrise)
        sunriseText     = view.findViewById(R.id.sunriseText)
        sunset          = view.findViewById(R.id.sunset)
        sunsetText      = view.findViewById(R.id.sunsetText)
        temp            = view.findViewById(R.id.temp)
        tempStatus      = view.findViewById(R.id.tempStatus)
        tempText        = view.findViewById(R.id.tempText)
        time            = view.findViewById(R.id.time)
        todStatus       = view.findViewById(R.id.todStatus)
        vis             = view.findViewById(R.id.vis)
        visStatus       = view.findViewById(R.id.visStatus)
        visText         = view.findViewById(R.id.visText)
        wind            = view.findViewById(R.id.wind)
        windDir         = view.findViewById(R.id.windDir)
        windStatus      = view.findViewById(R.id.windStatus)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // /////////////////////////////////////////////////////////////////////////////////////////
        // TODO: Change to use view and data binding.
        // /////////////////////////////////////////////////////////////////////////////////////////

        // Get the view model.
        viewModel = ViewModelProvider(this).get(SafeToFlyViewModel::class.java)

        // /////////////////////////////////////////////////////////////////////////////////////////
        // Add observer's to update the ui with the field's from SafeToFlyViewModel.
        // /////////////////////////////////////////////////////////////////////////////////////////

        viewModel.overallStatus.observe(viewLifecycleOwner, Observer { value ->
            updateStatus ()
        })
    }

    /**
     * Update Status
     */
    fun updateStatus ()
    {
        //  First update the overall status.
        updateOverallStatus ()

        // Now update the rest
        updateVisibility ()
        updateCloudCeiling ()
        updateTimeOfDay ()
        updateWindSpeed ()
        updateTemperature ()
        updatePrecipitation ()
    }

    /**
     * Update Overall Status
     */
    fun updateOverallStatus ()
    {
        when (viewModel.overallStatus.value?:Status.RED) {
            Status.RED      -> MainActivity.instance.overallStatusButton.setImageResource(R.drawable.drone_red)
            Status.YELLOW   -> MainActivity.instance.overallStatusButton.setImageResource(R.drawable.drone_yellow)
            Status.GREEN    -> MainActivity.instance.overallStatusButton.setImageResource(R.drawable.drone_green)
        }

        // Determine the city and state from the lat/lon
        val geocoder  = Geocoder(MainActivity.instance, Locale.getDefault())
        val addresses = geocoder.getFromLocation(viewModel.gpsLoc.latitude, viewModel.gpsLoc.longitude, 1)
        val address   = addresses[0]

        // City, State and zip
        location.text = getString (R.string.city_state_zip, address.locality, address.adminArea, address.postalCode)
    }

    /**
     * Update Visibility
     */
    private fun updateVisibility ()
    {
        when (viewModel.visibilityStatus) {
            Status.RED      -> visStatus.setImageResource(R.drawable.bino_red)
            Status.YELLOW   -> visStatus.setImageResource(R.drawable.bino_yellow)
            Status.GREEN    -> visStatus.setImageResource(R.drawable.bino_green)
        }

        vis.text = getString(R.string.visibility,viewModel.visibility.toString())
    }

    /**
     * Update CloudCeiling
     */
    private fun updateCloudCeiling ()
    {
        when (viewModel.cloudCeilingStatus) {
            Status.RED        -> cloudStatus.setImageResource(R.drawable.cloudy_red)
            Status.YELLOW     -> cloudStatus.setImageResource(R.drawable.cloudy_yellow)
            Status.GREEN      -> cloudStatus.setImageResource(R.drawable.cloudy_green)
        }

        cloudCeil.text        = getString(R.string.agl, viewModel.cloudCeiling.toString())

        val cloudCoverPercent:Int = (100.0 * viewModel.cloudCover).toInt()
        cloudCover.text       = getString(R.string.cloud_cover, cloudCoverPercent.toString())
    }

    /**
     * Update TimeOfDay
     */
    private fun updateTimeOfDay ()
    {
        when (viewModel.timeOfDayStatus) {
            Status.RED      -> todStatus.setImageResource(R.drawable.clock_red)
            Status.YELLOW   -> todStatus.setImageResource(R.drawable.clock_yellow)
            Status.GREEN    -> todStatus.setImageResource(R.drawable.clock_green)
        }

        sunrise.text  = dateFormat.format(viewModel.sunrise)
        sunset.text   = dateFormat.format(viewModel.sunset)
        time.text     = getString(R.string.dark_sky_time, dateFormat.format(viewModel.timeOfDay.time))

        val daylightLeft = viewModel.sunset.time - viewModel.timeOfDay.time

        // Compute the remaining daylight.
        var minutes = 0
        var hours   = 0

        if (daylightLeft>0) {
            val ms   = daylightLeft/1000.0f
            val ms60 = (ms / 60.0).toInt()
            minutes  = ms60 % 60
            hours    = ms60 / 60
        }

        // Format sunrise, sunset, time and daylight remaining
        daylight.text = getString(R.string.hour_min, hours.toString(), minutes.toString())

    }

    /**
     * Update WindSpeed
     */
    private fun updateWindSpeed ()
    {
        when (viewModel.windSpeedStatus) {
            Status.RED      -> windStatus.setImageResource(R.drawable.wind_speed_red)
            Status.YELLOW   -> windStatus.setImageResource(R.drawable.wind_speed_yellow)
            Status.GREEN    -> windStatus.setImageResource(R.drawable.wind_speed_green)
        }

        // Set gusts.
        gusts.text   = getString(R.string.gusts,viewModel.gusts.toString())

        // Set windspeed and compute wind dir.

        // Compute what eighth of circle we are in.
        val windy = viewModel.windDir / 45.0

        // Now set dir based on windy. (TODO Shame when won't work here. Pos make this section a function)
        var windDirection = getString(R.string.unknown)
        if (       windy <= 22.5  || windy >  337.5) {
            windDirection = getString (R.string.wind_dir_east)
        } else if (windy >  22.5  && windy <=  67.5) {
            windDirection = getString (R.string.wind_dir_north_east)
        } else if (windy >  67.5  && windy <= 112.5) {
            windDirection = getString (R.string.wind_dir_north)
        } else if (windy > 112.5  && windy <= 157.5) {
            windDirection = getString (R.string.wind_dir_north_west)
        } else if (windy > 157.5  && windy <= 202.5) {
            windDirection = getString (R.string.wind_dir_west)
        } else if (windy > 202.5  && windy <= 247.5) {
            windDirection = getString (R.string.wind_dir_south_west)
        } else if (windy > 247.5  && windy <= 292.5) {
            windDirection = getString (R.string.wind_dir_south)
        } else if (windy > 292.5  && windy <= 337.5) {
            windDirection = getString (R.string.wind_dir_south_east)
        }

        //
        windDir.text = getString(R.string.wind_dir, windDirection, viewModel.windDir.toString())
    }

    /**
     * Update Temperature
     */
    private fun updateTemperature ()
    {
        when (viewModel.tempStatus) {
            Status.RED      -> {
                tempStatus.setImageResource(R.drawable.tstorm_red)
            }
            Status.YELLOW   -> {
                tempStatus.setImageResource(R.drawable.tstorm_yellow)
            }
            Status.GREEN    -> {
                tempStatus.setImageResource(R.drawable.tstorm_green)
            }
        }

        // The current temperature
        temp.text = getString(R.string.temp, viewModel.temp.toString())

        // The current conditions
        tempText.text =viewModel.conditions
    }

    /**
     * Update Precipitation
     */
    private fun updatePrecipitation ()
    {
        when (viewModel.precipitationStatus) {
            Status.RED      -> rainDrop.setImageResource(R.drawable.drop_red)
            Status.YELLOW   -> rainDrop.setImageResource(R.drawable.drop_yellow)
            Status.GREEN    -> rainDrop.setImageResource(R.drawable.drop_green)
        }

        precipText.text = getString(R.string.precip, viewModel.precipitation.toString())
    }

}
