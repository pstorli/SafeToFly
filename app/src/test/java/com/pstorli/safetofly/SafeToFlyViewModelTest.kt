package com.pstorli.safetofly

import com.pstorli.safetofly.data.SafeToFlyViewModel
import com.pstorli.safetofly.util.Status
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

@RunWith(JUnit4::class)
class SafeToFlyViewModelTest {

    private lateinit var safeToFlyViewModel : SafeToFlyViewModel

    @Before
    fun setUp() {
        // Redirect output
        val os: OutputStream = ByteArrayOutputStream()
        val ps = PrintStream(os)
        System.setOut(ps)

        safeToFlyViewModel = SafeToFlyViewModel ()
    }

    @Test
    fun getVisibilityStatus() {
        System.out.println ("Testing Visibility")
        safeToFlyViewModel.visibility = safeToFlyViewModel.VISIBILITY_DEF
        safeToFlyViewModel.updateVisibility ()

        assertTrue (Status.GREEN == safeToFlyViewModel.visibilityStatus)
    }

    @Test
    fun getCloudCeilingStatus() {
        System.out.println ("Testing CloudCeiling")
        safeToFlyViewModel.cloudCeiling = safeToFlyViewModel.CLOUD_CEILING_DEF
        safeToFlyViewModel.updateCloudCeiling()

        assertTrue (Status.GREEN == safeToFlyViewModel.cloudCeilingStatus)
    }

    @Test
    fun getTimeOfDayStatus() {
        System.out.println ("Testing TimeOfDay")
        safeToFlyViewModel.timeOfDay = Date()
        safeToFlyViewModel.updateTimeOfDay()

        // The result changes withh the time of day.
        assertTrue (
            Status.RED == safeToFlyViewModel.timeOfDayStatus ||
            Status.YELLOW == safeToFlyViewModel.timeOfDayStatus ||
            Status.GREEN == safeToFlyViewModel.timeOfDayStatus
        )
    }

    @Test
    fun getWindSpeedStatus() {
        System.out.println ("Testing WindSpeed")
        safeToFlyViewModel.windSpeed = safeToFlyViewModel.WIND_SPEED
        safeToFlyViewModel.updateWindSpeed()

        assertTrue (Status.GREEN == safeToFlyViewModel.windSpeedStatus)
    }

    @Test
    fun getTempStatus() {
        System.out.println ("Testing Temp")
        safeToFlyViewModel.temp = safeToFlyViewModel.TEMP_DEF
        safeToFlyViewModel.updateTemperature()

        assertTrue (Status.GREEN == safeToFlyViewModel.tempStatus)
    }

    @Test
    fun getPrecipitationStatus() {
        System.out.println ("Testing Precipitation")
        safeToFlyViewModel.precipitation = safeToFlyViewModel.PRECIPITATION_DEF
        safeToFlyViewModel.updatePrecipitation()

        assertTrue (Status.GREEN == safeToFlyViewModel.precipitationStatus)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        // Restore normal output
        val originalOut = System.out
        System.setOut(originalOut)
    }
}