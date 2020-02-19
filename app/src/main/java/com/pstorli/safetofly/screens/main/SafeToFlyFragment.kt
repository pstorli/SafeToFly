package com.pstorli.safetofly.screens.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pstorli.safetofly.R
import com.pstorli.safetofly.data.SafeToFlyViewModel
import com.pstorli.safetofly.util.Status

class SafeToFlyFragment (imageButton: ImageButton?) : Fragment() {
    private lateinit var viewModel: SafeToFlyViewModel

    private var overallStatusButton: ImageButton? // This can be null.

    // TODO Change to use new Google binding feature will eliminate these. https://developer.android.com/topic/libraries/view-binding
    // Fields with ids from safe_to_fly_fragment.xml
    private lateinit var binos:         ImageView
    private lateinit var cloudCeil:     TextView
    private lateinit var cloudCover:    TextView
    private lateinit var cloudStatus:   ImageView
    private lateinit var cloudText:     TextView
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
    private lateinit var todIcon:       ImageView
    private lateinit var todStatus:     ImageView
    private lateinit var vis:           TextView
    private lateinit var visStatus:     ImageView
    private lateinit var visText:       TextView
    private lateinit var wind:          TextView
    private lateinit var windDir:       TextView
    private lateinit var windStatus:    ImageView

    init {
        // Store this so we can update it.
        overallStatusButton = imageButton
    }

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
        // TODO: Change o use view and data binding.
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
            Status.RED      -> overallStatusButton?.setImageResource(R.drawable.drone_red)
            Status.YELLOW   -> overallStatusButton?.setImageResource(R.drawable.drone_yellow)
            Status.GREEN    -> overallStatusButton?.setImageResource(R.drawable.drone_green)
        }
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

        vis.text = viewModel.visibility.toString()+"M"
    }

    /**
     * Update CloudCeiling
     */
    private fun updateCloudCeiling ()
    {
        when (viewModel.cloudCeilingStatus) {
            Status.RED      -> cloudStatus.setImageResource(R.drawable.cloudy_red)
            Status.YELLOW   -> cloudStatus.setImageResource(R.drawable.cloudy_yellow)
            Status.GREEN    -> cloudStatus.setImageResource(R.drawable.cloudy_green)
        }

        cloudCeil.text  = viewModel.cloudCeiling.toString() + "AGL"
        cloudCover.text = (100 * viewModel.cloudCeiling).toString() + "%"

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

        daylight.text = viewModel.daylight
        sunrise.text  = viewModel.sunrise.toString()
        sunset.text   = viewModel.sunset.toString()
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

        windDir.text = viewModel.windDir.toString() // TODO SW (220)
        gusts.text   = "Gusts" + viewModel.gusts.toString() + "MPH" // TODO - Use resource string w/param for gust
    }

    /**
     * Update Temperature
     */
    private fun updateTemperature ()
    {
        when (viewModel.tempStatus) {
            Status.RED      -> tempStatus.setImageResource(R.drawable.tstorm_red)
            Status.YELLOW   -> tempStatus.setImageResource(R.drawable.tstorm_yellow)
            Status.GREEN    -> tempStatus.setImageResource(R.drawable.tstorm_green)
        }

        temp.text = viewModel.temperature.toString() + "F" // TODO - Use resource string w/param for temp
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

        precipText.text = viewModel.precipitation.toString() + "%" // TODO - Use resource string w/param for precip
    }

}
