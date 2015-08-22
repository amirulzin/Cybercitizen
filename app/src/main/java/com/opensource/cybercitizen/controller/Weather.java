package com.opensource.cybercitizen.controller;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class Weather
{
    private static final String apiKey = "d18f8f6d7b14ee4308e53d3f87247d71";
    private static final int cityId = 6697380;

    private final String endpoint = "http://api.openweathermap.org/data/2.5/weather?id=6697380&appid=d18f8f6d7b14ee4308e53d3f87247d71";

    public void getWeather()
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
                String body = response.body().string();

            }
        });
    }
}
