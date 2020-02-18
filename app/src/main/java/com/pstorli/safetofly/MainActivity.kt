package com.pstorli.safetofly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pstorli.safetofly.screens.main.SafeToFlyFragment

class MainActivity : AppCompatActivity() {

    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add Walmart splash icon to toolbar.
        val actionBar = getSupportActionBar()
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setIcon(R.drawable.ic_launcher)
        }

        showFragment (SafeToFlyFragment())
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
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)

        // Finishing the transition
        transaction.commit()
    }
}
