package com.pstorli.safetofly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.pstorli.safetofly.screens.main.SafeToFlyFragment

class MainActivity : AppCompatActivity() {

    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var imageButton: ImageButton? = null
        val actionBar = getSupportActionBar()
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            val mInflater = LayoutInflater.from(this)

            val customView = mInflater.inflate(R.layout.custom_actionbar, null)

            imageButton = customView.findViewById(R.id.imageButton) as ImageButton
            imageButton.setOnClickListener(object : View.OnClickListener {

                override fun onClick(view: View) {
                    val currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                    if (currentFragment is SafeToFlyFragment) {
                        currentFragment.updateStatus()
                    }
                }
            })

            actionBar.setCustomView(customView)
            actionBar.setDisplayShowCustomEnabled(true)
        }

        showFragment (SafeToFlyFragment(imageButton))
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
}
