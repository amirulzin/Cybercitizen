package com.opensource.cybercitizen.activity.model;

import java.util.List;

public class Merchant
{
    String name, latitude, longitude, type;
    List<Demand> mActualDemands;
    List<Demand> mForecastDemands;

    public Merchant()
    {
    }

    public List<Demand> getActualDemands()
    {
        return mActualDemands;
    }

    public void setActualDemands(final List<Demand> actualDemands)
    {
        mActualDemands = actualDemands;
    }

    public List<Demand> getForecastDemands()
    {
        return mForecastDemands;
    }

    public void setForecastDemands(final List<Demand> forecastDemands)
    {
        mForecastDemands = forecastDemands;
    }

    public static class Demand
    {
        int day = -1;
        int hour = -1;
        int congestion = -1;

        public Demand(final String day, final int hour, final int congestion)
        {
            this.day = Integer.parseInt(day.substring(2));
            this.hour = hour;
            this.congestion = congestion;
        }

        public int getDay()
        {
            return day;
        }

        public int getHour()
        {
            return hour;
        }

        public int getCongestion()
        {
            return congestion;
        }

        @Override
        public String toString()
        {
            return "Demand{" +
                    "day=" + day +
                    ", hour=" + hour +
                    ", congestion=" + congestion +
                    '}';
        }
    }

}
