package com.jankapotamus.darkskyandroidwrapper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlie on 10/16/16.
 */

public class DayConditions {

    private int time;
    private String summary;

    @SerializedName("icon")
    private String iconString;

    private int sunriseTime;
    private int sunsetTime;
    private double moonPhase;
    private double precipIntensity;
    private double precipIntensityMax;
    private int precipIntensityMaxTime;
    private double precipProbability;
    private String precipType;
    private double temperatureMin;
    private int temperatureMinTime;
    private double temperatureMax;
    private int temperatureMaxTime;
    private double apparentTemperatureMin;
    private int apparentTemperatureMinTime;
    private double apparentTemperatureMax;
    private int apparentTemperatureMaxTime;
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

    public int getSunriseTime() {
        return sunriseTime;
    }

    public int getSunsetTime() {
        return sunsetTime;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public int getPrecipIntensityMaxTime() {
        return precipIntensityMaxTime;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public int getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public int getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public int getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public int getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
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
        return "{DayConditions: " +
                "[Time = " + time +
                "][Summary = " + summary +
                "][IconString = " + iconString +
                "][SunriseTime = " + sunriseTime +
                "][SunsetTime = " + sunsetTime +
                "][MoonPhase = " + moonPhase +
                "][PrecipIntensity = " + precipIntensity +
                "][PrecipIntensityMax = " + precipIntensityMax +
                "][PrecipIntensityMaxTime = " + precipIntensityMaxTime +
                "][PrecipProbability = " + precipProbability +
                "][PrecipType = " + precipType +
                "][TemperatureMin = " + temperatureMin +
                "][TemperatureMinTime = " + temperatureMinTime +
                "][TemperatureMax = " + temperatureMax +
                "][TemperatureMaxTime = " + temperatureMaxTime +
                "][ApparentTemperatureMin = " + apparentTemperatureMin +
                "][ApparentTemperatureMinTime = " + apparentTemperatureMinTime +
                "][ApparentTemperatureMax = " + apparentTemperatureMax +
                "][ApparentTemperatureMaxTime = " + apparentTemperatureMaxTime +
                "][DewPoint = " + dewPoint + "][Humidity = " + humidity +
                "][WindSpeed = " + windSpeed +
                "][WindBearing = " + windBearing +
                "][Visibility = " + visibility +
                "][CloudCover = " + cloudCover +
                "][Pressure = " + pressure +
                "][Ozone = " + ozone + "]}";
    }
}
