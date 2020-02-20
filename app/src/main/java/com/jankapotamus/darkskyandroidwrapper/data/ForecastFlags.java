package com.jankapotamus.darkskyandroidwrapper.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Charlie on 10/23/16.
 */

public class ForecastFlags {

    private List<String> sources;

    @SerializedName("darksky-stations")
    private List<String> darkSkyStations;

    @SerializedName("lamp-stations")
    private List<String> lampStations;

    @SerializedName("isd-stations")
    private List<String> isdStations;

    @SerializedName("madis-stations")
    private List<String> madisStations;

    private String units;


    public List<String> getSources() {
        return sources;
    }

    public List<String> getDarkSkyStations() {
        return darkSkyStations;
    }

    public List<String> getLampStations() {
        return lampStations;
    }

    public List<String> getIsdStations() {
        return isdStations;
    }

    public List<String> getMadisStations() {
        return madisStations;
    }

    public String getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "{Flags: " +
                "[Sources = " + sources +
                "][DarkSky Stations = " + darkSkyStations +
                "][LAMP Stations = " + lampStations +
                "][ISD Stations = " + isdStations +
                "][MADIS Stations = " + madisStations +
                "][Units = " + units + "]";
    }
}
