package com.pstorli.safetofly

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.pstorli.safetofly.screens.main.SafeToFlyFragment


class MainActivity : AppCompatActivity() {

    lateinit var overallStatusButton: ImageButton
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var customView: View

    /**
     * Instance of MainActivity
     */
    companion object {
        private lateinit var inst: MainActivity

        val instance: MainActivity
            get() {
                return inst
            }
    }

    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Remember ourself.
        inst = this

        // GPS Helper
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val actionBar = getSupportActionBar()
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            val mInflater = LayoutInflater.from(this)

            customView = mInflater.inflate(R.layout.custom_actionbar, null)

            overallStatusButton = customView.findViewById(R.id.imageButton) as ImageButton
            overallStatusButton.setOnClickListener(object : View.OnClickListener {

                override fun onClick(view: View) {
                    refreshData ()
                }
            })

            actionBar.setCustomView(customView)
            actionBar.setDisplayShowCustomEnabled(true)

            Snackbar.make(customView, getString (R.string.click_plane), Snackbar.LENGTH_LONG).show()
        }

        showFragment (SafeToFlyFragment())
    }

    /**
     * Update the gps location and re-get and dispolay new weather data.
     */
    fun refreshData ()
    {
        // Get Permission first.
        if (checkPermission ()) {
            // Where are we?
            getLocation()
        }

        // What's the weather?
        val safeToFlyFragment = getSafeToFlyFragment ()
        safeToFlyFragment?.updateStatus()
    }

    fun getSafeToFlyFragment () : SafeToFlyFragment?
    {
        var safeToFlyFragment: SafeToFlyFragment? = null
        val currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container)
        if (currentFragment is SafeToFlyFragment) {
            safeToFlyFragment = currentFragment
        }

        return safeToFlyFragment
    }

    /**
     * Show a fragment.
     */
    fun showFragment (fragment: androidx.fragment.app.Fragment)
    {
        // Get the fragment support manager instance
        val manager = supportFragmentManager

        // Begin the fragment transition using support fragment manager
        val transaction = manager.beginTransaction()

        // Replace the fragment on container
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

        // Finishing the transition
        transaction.commit()
    }

    /**
     * Check the permission to access GPS
     */
    fun checkPermission () : Boolean
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    fun getLocation ()
    {
        val task: Task<Location> = fusedLocationClient.getLastLocation()
        task.addOnSuccessListener { location ->
            val safeToFlyFragment = getSafeToFlyFragment ()

            // Got location?
            if (location != null) {
                safeToFlyFragment?.viewModel?.location = location
                Snackbar.make(
                    customView,
                    getString (R.string.lat_lon, location.latitude.toString(), location.longitude.toString()),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
