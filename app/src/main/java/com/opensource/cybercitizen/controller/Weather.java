package com.opensource.cybercitizen.controller;

import android.support.annotation.Nullable;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Weather
{
    private static final String apiKey = "d18f8f6d7b14ee4308e53d3f87247d71";
    private static final int cityId = 6697380;
    private final String endpoint = "http://api.openweathermap.org/data/2.5/weather?id=6697380&appid=d18f8f6d7b14ee4308e53d3f87247d71";
    boolean processing = true;
    private ConditionCode mCurrentCode = ConditionCode.SKY_IS_CLEAR;

    public String getEndpoint()
    {
        return endpoint;
    }

    public void getWeather(@Nullable final Listener listener)
    {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(endpoint).build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {

            }

            @Override
            public void onResponse(final Response response) throws IOException
            {
                processing = false;
                String body = response.body().string();
                try
                {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray inner = jsonObject.getJSONArray("weather");
                    if (inner != null)
                    {
                        //Log.v("testtag", "L:" + inner.length() + " " + inner.toString() + " IL:" + inner.getJSONObject(0).toString());
                        int out = inner.getJSONObject(0).getInt("id");
                        mCurrentCode = ConditionCode.valueof(out);
                        //Log.v("testtag", out);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (listener != null)
                {
                    listener.onWeatherReady(Weather.this);

                }
            }
        });
    }

    public boolean isSunnyCurrently()
    {
        return mCurrentCode.getId() >= 800 && mCurrentCode.getId() < 900;
    }

    public boolean isRainingCurrently()
    {
        return mCurrentCode.getId() >= 200 && mCurrentCode.getId() < 600;
    }

    public boolean isProcessing()
    {
        return processing;
    }

    public enum ConditionCode
    {
        UNKNOWN(Integer.MIN_VALUE),
        /* Thunderstorm */
        THUNDERSTORM_WITH_LIGHT_RAIN(200),
        THUNDERSTORM_WITH_RAIN(201),
        THUNDERSTORM_WITH_HEAVY_RAIN(202),
        LIGHT_THUNDERSTORM(210),
        THUNDERSTORM(211),
        HEAVY_THUNDERSTORM(212),
        RAGGED_THUNDERSTORM(221),
        THUNDERSTORM_WITH_LIGHT_DRIZZLE(230),
        THUNDERSTORM_WITH_DRIZZLE(231),
        THUNDERSTORM_WITH_HEAVY_DRIZZLE(232),
        /* Drizzle */
        LIGHT_INTENSITY_DRIZZLE(300),
        DRIZZLE(301),
        HEAVY_INTENSITY_DRIZZLE(302),
        LIGHT_INTENSITY_DRIZZLE_RAIN(310),
        DRIZZLE_RAIN(311),
        HEAVY_INTENSITY_DRIZZLE_RAIN(312),
        SHOWER_DRIZZLE(321),
        /* Rain */
        LIGHT_RAIN(500),
        MODERATE_RAIN(501),
        HEAVY_INTENSITY_RAIN(502),
        VERY_HEAVY_RAIN(503),
        EXTREME_RAIN(504),
        FREEZING_RAIN(511),
        LIGHT_INTENSITY_SHOWER_RAIN(520),
        SHOWER_RAIN(521),
        HEAVY_INTENSITY_SHOWER_RAIN(522),
        /* Snow */
        LIGHT_SNOW(600),
        SNOW(601),
        HEAVY_SNOW(602),
        SLEET(611),
        SHOWER_SNOW(621),
        /* Atmosphere */
        MIST(701),
        SMOKE(711),
        HAZE(721),
        SAND_OR_DUST_WHIRLS(731),
        FOG(741),
        /* Clouds */
        SKY_IS_CLEAR(800),
        FEW_CLOUDS(801),
        SCATTERED_CLOUDS(802),
        BROKEN_CLOUDS(803),
        OVERCAST_CLOUDS(804),
        /* Extreme */
        TORNADO(900),
        TROPICAL_STORM(901),
        HURRICANE(902),
        COLD(903),
        HOT(904),
        WINDY(905),
        HAIL(906);

        private int id;

        ConditionCode(int code)
        {
            this.id = code;
        }

        static public ConditionCode valueof(int id)
        {
            for (ConditionCode condition : ConditionCode.values())
            {
                if (condition.id == id)
                    return condition;
            }
            return ConditionCode.UNKNOWN;
        }

        public int getId()
        {
            return this.id;
        }
    }

    public interface Listener
    {
        void onWeatherReady(Weather weather);
    }

}
