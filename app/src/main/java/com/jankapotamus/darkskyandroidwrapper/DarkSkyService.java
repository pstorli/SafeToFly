package com.jankapotamus.darkskyandroidwrapper;

import com.jankapotamus.darkskyandroidwrapper.data.Forecast;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Charlie on 10/14/16.
 */

public interface DarkSkyService {

    @GET("/mock-data.json")
    Call<Forecast> getStaticForecast();

    @GET("/forecast/{apiKey}/{lat},{long}")
    Call<Forecast> getForecast(@Path("apiKey") String apiKey,
                               @Path("lat") double latitude,
                               @Path("long") double longitude,
                               @Nullable @QueryMap Map<String, String> options);

    @GET("/forecast/{apiKey}/{lat},{long},{time}")
    Call<Forecast> getTimeMachineForecast(@Path("apiKey") String apiKey,
                                          @Path("lat") double latitude,
                                          @Path("long") double longitude,
                                          @Path("time") double timestamp,
                                          @Nullable @QueryMap Map<String, String> options);
}
