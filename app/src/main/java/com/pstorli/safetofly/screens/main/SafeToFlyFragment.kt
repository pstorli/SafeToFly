package com.pstorli.safetofly.screens.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.pstorli.safetofly.R
import com.pstorli.safetofly.data.SafeToFlyViewModel
import com.pstorli.safetofly.util.Status
import java.time.LocalDateTime

class SafeToFlyFragment : Fragment() {

    companion object {
        fun newInstance() =
            SafeToFlyFragment()
    }

    private lateinit var viewModel: SafeToFlyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.safe_to_fly_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // /////////////////////////////////////////////////////////////////////////////////////////
        // TODO: Change o use view and data binding.
        // /////////////////////////////////////////////////////////////////////////////////////////

        // Get the view model.
        viewModel = ViewModelProviders.of(this).get(SafeToFlyViewModel::class.java)

        // /////////////////////////////////////////////////////////////////////////////////////////
        // Add observer's to update the ui with the field's from SafeToFlyViewModel.
        // /////////////////////////////////////////////////////////////////////////////////////////

        viewModel.overallStatus.observe(viewLifecycleOwner, Observer { value ->
            updateStatus (value)
        })
    }

    /**
     * Update Status
     */
    private fun updateStatus (value: Status)
    {
        //  First update
    }

    /**
     * Update Visibility
     */
    private fun updateVisibility (value: Status)
    {

    }

    /**
     * Update CloudCeiling
     */
    private fun updateCloudCeiling (value: Status)
    {

    }

    /**
     * Update TimeOfDay
     */
    private fun updateTimeOfDay (value: Status)
    {

    }

    /**
     * Update WindSpeed
     */
    private fun updateWindSpeed (value: Status)
    {

    }

    /**
     * Update Temperature
     */
    private fun updateTemperature (value: Status)
    {

    }

    /**
     * Update Precipitation
     */
    private fun updatePrecipitation (value: Status)
    {

    }

}
