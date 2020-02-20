package com.jankapotamus.darkskyandroidwrapper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlie on 10/16/16.
 */

public class CurrentConditions {

    private int time;
    private String summary;
    @SerializedName("icon")
    private String iconString;
    private int nearestStormDistance;
    private int nearestStormBearing;
    private double precipIntensity;
    private double precipProbability;
    private double temperature;
    private double apparentTemperature;
    private double dewPoint;
    private double humidity;
    private double windSpeed;
    private int windBearing;
    private double visibility;
    private double cloudCover;
    private double pressure;
    private double ozone;


    public int getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIconString() {
        return iconString;
    }

    public int getNearestStormDistance() {
        return nearestStormDistance;
    }

    public int getNearestStormBearing() {
        return nearestStormBearing;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWindBearing() {
        return windBearing;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public double getPressure() {
        return pressure;
    }

    public double getOzone() {
        return ozone;
    }

    @Override
    public String toString() {
        return "{CurrentConditions: [Time = " + time + "][Summary = " + summary + "][IconString = "
                + iconString + "][NearestStormDistance = " + nearestStormDistance + "][NearestStormBearing = "
                + nearestStormBearing + "][PrecipIntensity = " + precipIntensity + "][PrecipProbability = "
                + precipProbability + "][Temperature = " + temperature + "][ApparentTemperature = "
                + apparentTemperature + "][DewPoint = " + dewPoint + "][Humidity = " + humidity
                + "][WindSpeed = " + windSpeed + "][WindBearing = " + windBearing + "][Visibility = "
                + visibility + "][CloudCover = " + cloudCover + "][Pressure = " + pressure + "][Ozone = "
                + ozone + "]}";
    }
}
