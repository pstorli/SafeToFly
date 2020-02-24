package com.pstorli.safetofly

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.pstorli.safetofly.data.SafeToFlyViewModel
import com.pstorli.safetofly.screens.main.SafeToFlyFragment

class MainActivity : AppCompatActivity() {
    // Consts
    val LOCATION_REQUEST_CODE = 42

    // Vars
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

    init {
        inst = this
    }

    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // GPS Helper
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val actionBar = supportActionBar
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

            actionBar.customView = customView
            actionBar.setDisplayShowCustomEnabled(true)

            val msg:String = getString (R.string.click_plane)
            val clickListener = DialogInterface.OnClickListener{_,which ->
                // We don't care which button they pressed, just refresh the data.
                refreshData ()
            }

            // Show dialog to explain about refresh data.
            showMessage (msg, clickListener)
        }

        showFragment (SafeToFlyFragment())
    }

    /**
     * Update the gps location and re-get and dispolay new weather data.
     */
    fun refreshData ()
    {
        // Get Permission first.
        if (checkPermission()) {
            // Where are we?
            getLocation()
        }
        else {
            requestPermission ()
        }
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

    fun getSafeToFlyFragment () : SafeToFlyFragment?
    {
        var safeToFlyFragment: SafeToFlyFragment? = null
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is SafeToFlyFragment) {
            safeToFlyFragment = currentFragment
        }

        return safeToFlyFragment
    }

    /**
     * Check the permission to access GPS
     */
    fun checkPermission () : Boolean
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    fun getLocation ()
    {
        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            // Got location?
            if (location != null) {
                SafeToFlyViewModel.instance.gpsLoc = location

                // Get new data from Dark Sky. What's the weather?
                SafeToFlyViewModel.instance.getData()

                // Show the location in the snackbar.
                showSnackBar (getString (R.string.lat_lon, location.latitude.toString(), location.longitude.toString()))
            }
        }
    }

    /**
     * Show the text passed in as a snackbar.
     */
    fun showSnackBar (text:String)
    {
        Snackbar.make(
            customView,
            text,
            Snackbar.LENGTH_LONG).show()
    }

    /**
    * Show the text passed in as a snackbar.
    */
    fun showMessage (text:String, clickListener:DialogInterface.OnClickListener) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(text)

            // if the dialog is cancelable
            .setCancelable(false)

            // positive button text and action
            .setPositiveButton(getString(R.string.ok), clickListener)

            // negative button text and action
            .setNegativeButton(getString(R.string.cancel), clickListener)

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(getString(R.string.question))
        // show alert dialog
        alert.show()
    }

    /**
     * Ask the user for permission.
     */
    fun requestPermission () {
        ActivityCompat.requestPermissions(
            this,
            arrayOf (Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE)
    }


    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                // Get permission decision.
                val isPermissionsGranted = grantResults[0] != PackageManager.PERMISSION_GRANTED

                // Was it granted?
                if(isPermissionsGranted){
                    // Get fresh data.
                    refreshData ()

                    // Show granted snackbar.
                    showSnackBar(getString(R.string.permission_granted))
                }else{
                    // Show denied snackbar.
                    showSnackBar(getString(R.string.permission_denied))
                }
                return
            }
        }
    }
}