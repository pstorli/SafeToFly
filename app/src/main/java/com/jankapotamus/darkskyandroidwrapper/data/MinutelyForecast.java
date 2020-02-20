package com.jankapotamus.darkskyandroidwrapper.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Charlie on 10/16/16.
 */

public class MinutelyForecast {

    private String summary;

    @SerializedName("icon")
    private String iconString;

    @SerializedName("data")
    private List<MinuteConditions> minutelyConditionsList;


    public String getSummary() {
        return summary;
    }

    public String getIconString() {
        return iconString;
    }

    public List<MinuteConditions> getMinutelyConditionsList() {
        return minutelyConditionsList;
    }

    @Override
    public String toString() {
        return "{MinutelyForecast: [Summary = " + summary + "][IconString = " + iconString +
                "][MinutelyConditionsList = " + minutelyConditionsList.toString() + "]}";
    }
}
