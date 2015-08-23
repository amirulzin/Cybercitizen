package com.opensource.cybercitizen.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.model.Merchant;
import com.opensource.cybercitizen.base.NestedScrollHomeBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MerchantHomeActivity extends NestedScrollHomeBaseActivity
{

    private BarChart mBarChart;

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        getCollapsingToolbarLayout().setTitle("Restoran Mat Ayam Kampung");
        Glide.with(this).load(R.drawable.uc_ayammat_header).into(getHeaderImageView());

        View baseView = inflateNestedContent(R.layout.activity_merchant);
        int day = 7;
        Merchant merchant = getMerchant();

        mBarChart = (BarChart) baseView.findViewById(R.id.am_chart);

        mBarChart.post(new Runnable()
        {
            @Override
            public void run()
            {
                mBarChart.animateY(2500, Easing.EasingOption.EaseInQuad);
            }
        });
        mBarChart.setAutoScaleMinMaxEnabled(true);
        List<BarEntry> actualEntry = new ArrayList<>();
        int maxActualCongestion = 0;
        List<String> xAxis = new ArrayList<>();
        for (int i = 1; i < 25; i++)
        {
            xAxis.add(String.valueOf(i));
        }
        for (Merchant.Demand actualDemand : merchant.getActualDemands())
        {
            if (actualDemand.getDay() == day)
            {
                if (maxActualCongestion <= actualDemand.getCongestion())
                {
                    maxActualCongestion = actualDemand.getCongestion();
                }

                BarEntry entry = new BarEntry(actualDemand.getCongestion(), actualDemand.getHour());
                actualEntry.add(entry);

            }
        }
        final String yLabel = "Congestion per hour on " + day + " Aug";
        BarDataSet actualBarDataSet = new BarDataSet(actualEntry, yLabel);
        final BarData actualData = new BarData(xAxis, actualBarDataSet);


        List<BarEntry> forecastEntries = new ArrayList<>();
        for (Merchant.Demand forecastDemand : merchant.getForecastDemands())
        {
            if (forecastDemand.getDay() == day)
            {
                BarEntry entry = new BarEntry(forecastDemand.getCongestion(), forecastDemand.getHour());
                forecastEntries.add(entry);
            }
        }
        BarDataSet forecastDataSet = new BarDataSet(forecastEntries, yLabel);
        final BarData forecastData = new BarData(xAxis, forecastDataSet);


        mBarChart.setData(actualData);
        mBarChart.setDescription(null);
        mBarChart.getAxisRight().setDrawGridLines(false);


        ((TextView) findViewById(R.id.am_congestion_highest)).setText(String.valueOf(maxActualCongestion));
        ((TextView) findViewById(R.id.am_congestion_lowest)).setText(String.format("%.1f", actualData.getYValueSum() / actualData.getXValCount()));


        findViewById(R.id.am_actual).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                mBarChart.setData(actualData);
                mBarChart.animateY(1000, Easing.EasingOption.EaseInQuad);
            }
        });


        findViewById(R.id.am_forecast).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                mBarChart.setData(forecastData);
                mBarChart.animateY(1000, Easing.EasingOption.EaseInQuad);
            }
        });

    }


    @Override
    public int getDrawerItemId()
    {
        return 0;
    }

    public Merchant getMerchant()
    {
        Merchant merchant = new Merchant();
        {
            String dates = "0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0801,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0802,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0803,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0804,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0805,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0806,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0807,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0808,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0809,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0810,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0811,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0812,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0813,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0814,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815,0815";

            String[] dateArr = dates.split(",");

            int[] hours = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};

            int[] congestions = new int[]{
                    0, 0, 0, 0, 0, 11, 16, 49, 10, 41, 45, 24, 14, 13, 39, 22, 35, 5, 37, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 29, 2, 50, 16, 9, 49, 37, 11, 6, 18, 33, 44, 8, 48, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 28, 36, 4, 19, 1, 49, 48, 32, 49, 32, 25, 22, 32, 46, 0, 0, 0, 0, 0, 0, 0, 0, 0, 47, 37, 2, 11, 18, 48, 10, 1, 33, 21, 47, 17, 38, 50, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 26, 18, 12, 21, 19, 48, 46, 38, 18, 25, 37, 49, 46, 2, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 50, 28, 4, 4, 14, 39, 41, 8, 37, 31, 24, 37, 1, 37, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 31, 28, 18, 28, 32, 15, 31, 10, 10, 9, 8, 33, 15, 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 6, 5, 47, 35, 15, 13, 19, 1, 2, 5, 46, 10, 28, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 41, 34, 6, 7, 24, 1, 38, 34, 40, 46, 9, 33, 32, 29, 43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 14, 38, 41, 35, 7, 38, 31, 2, 3, 18, 42, 23, 29, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 29, 46, 5, 17, 29, 24, 11, 21, 35, 2, 23, 16, 36, 47, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 2, 29, 36, 1, 34, 9, 15, 5, 7, 16, 18, 49, 24, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 38, 14, 10, 49, 23, 35, 22, 17, 21, 1, 33, 9, 26, 10, 41, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 2, 40, 28, 7, 24, 22, 40, 35, 17, 25, 5, 45, 40, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 42, 9, 45, 14, 22, 21, 23, 30, 33, 16, 33, 37, 46, 35, 0, 0, 0
            };

            if (dateArr.length == congestions.length && hours.length == congestions.length)
            {
                ArrayList<Merchant.Demand> actualDemands = new ArrayList<>(dateArr.length);
                for (int i = 0; i < dateArr.length; i++)
                {
                    actualDemands.add(new Merchant.Demand(dateArr[i], hours[i], congestions[i]));
                }
                merchant.setActualDemands(actualDemands);

            }
            else
                throw new IllegalStateException("Wrong array length " + hours.length + " " + congestions.length + " " + dateArr.length);
        }

        {
            String dates = "0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0816,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0817,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0818,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0819,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820,0820";

            String[] dateArr = dates.split(",");

            int[] hours = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};


            int[] congestions = new int[]{0, 0, 0, 0, 0, 0, 2, 1, 4, 36, 3, 16, 33, 9, 20, 2, 26, 31, 49, 47, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 45, 44, 20, 43, 32, 20, 3, 47, 2, 33, 22, 3, 39, 48, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 44, 3, 4, 46, 29, 47, 9, 35, 17, 45, 29, 37, 20, 12, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 32, 9, 23, 28, 36, 24, 3, 46, 18, 34, 7, 8, 35, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 25, 20, 40, 15, 36, 42, 26, 29, 24, 29, 10, 6, 32, 19, 0, 0, 0
            };

            if (dateArr.length == congestions.length && hours.length == congestions.length)
            {
                ArrayList<Merchant.Demand> forecastedDemands = new ArrayList<>(dateArr.length);
                for (int i = 0; i < dateArr.length; i++)
                {
                    forecastedDemands.add(new Merchant.Demand(dateArr[i], hours[i], congestions[i]));
                }
                merchant.setForecastDemands(forecastedDemands);

            }
            else
                throw new IllegalStateException("Wrong array length " + hours.length + " " + congestions.length + " " + dateArr.length);

        }

        return merchant;

    }


}
