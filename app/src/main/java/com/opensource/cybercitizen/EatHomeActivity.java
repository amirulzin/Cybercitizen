package com.opensource.cybercitizen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EatHomeActivity extends HomeBaseActivity
{
    private final List<EatItem> mEatItems = new ArrayList<>(128);
    private final String ACTIVITY_MAIN_TITLE = "EAT";
    private RecyclerView.Adapter<EatItem.ViewHolder> mRecyclerAdapter = new RecyclerView.Adapter<EatItem.ViewHolder>()
    {
        @Override
        public EatItem.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
        {
            return new EatItem.ViewHolder(LayoutInflater.from(EatHomeActivity.this).inflate(EatItem.ViewHolder.getLayoutRes(), parent, false));
        }

        @Override
        public void onBindViewHolder(final EatItem.ViewHolder holder, final int position)
        {
            Log.d(ACTIVITY_MAIN_TITLE, " loading item " + position);
            holder.bindData(mEatItems.get(position), EatHomeActivity.this);
        }

        @Override
        public int getItemCount()
        {
            return mEatItems.size();
        }
    };

    public void setupList()
    {
        final EatItem starbucks = new EatItem("http://i.imgur.com/ydXZcaU.png", "Starbucks").setCongestion(50).setWifiAvailable(true);

        final EatItem kfc = new EatItem("http://i.imgur.com/qSBRJw3.png", "Kentucky Fried Chicken").setCongestion(90).setWifiAvailable(true);

        final EatItem mcD = new EatItem("http://i.imgur.com/7GaUg0d.png", "McDonalds").setCongestion(10).setWifiAvailable(true).setPromo("Lunch hour promotion\nEnding in 27 minutes");

        final EatItem pizzaHut = new EatItem("http://i.imgur.com/NpzwdWP.png", "Pizza Hut").setCongestion(10).setWifiAvailable(false);

        mEatItems.addAll(Arrays.asList(kfc, mcD, starbucks, pizzaHut));
    }


    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        setupList();
        getCollapsingToolbarLayout().setTitle(ACTIVITY_MAIN_TITLE);
        final RecyclerView recyclerView = getRecyclerView();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EatHomeActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public int getDrawerItemId()
    {
        return R.menu.menu_drawer;
    }

    public static class EatItem
    {
        String imageUri;
        String header;
        String wifistatus;
        String congestion;
        String promo;
        int timedPromo = -1;
        private boolean mWifiAvailable = false;

        public EatItem(final String imageUri, final String header)
        {
            this.imageUri = imageUri;
            this.header = header;
        }


        public EatItem(final String imageUri, final String header, final String wifistatus, final String congestion, final String promo)
        {
            this.imageUri = imageUri;
            this.header = header;
            this.wifistatus = wifistatus;
            this.congestion = congestion;
            this.promo = promo;
        }

        public EatItem()
        {
        }

        public int getTimedPromo()
        {
            return timedPromo;
        }

        public EatItem setDailyTimedPromo(int endsInMinutes)
        {
            timedPromo = endsInMinutes;
            return this;
        }

        public boolean isWifiAvailable()
        {
            return mWifiAvailable;
        }

        public EatItem setWifiAvailable(boolean available)
        {
            if (available)
            {
                wifistatus = "WiFi Available";
                mWifiAvailable = true;
            }

            else
            {
                wifistatus = "WiFi Not Available";
                mWifiAvailable = false;
            }
            return this;
        }

        public String getImageUri()
        {
            return imageUri;
        }

        public String getHeader()
        {
            return header;
        }

        public String getWifiStatus()
        {
            return wifistatus;
        }


        public String getCongestion()
        {
            return congestion;
        }

        public EatItem setCongestion(final int congestion)
        {
            this.congestion = String.valueOf(congestion) + "% Congestion";
            return this;
        }

        public String getPromo()
        {
            return promo;
        }

        public EatItem setPromo(final String promo)
        {
            this.promo = promo;
            return this;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            private static final int LAYOUT_RES = R.layout.listitem_eathome;
            private ImageView mImageView;
            private TextView mHeader, mWifistatus, mPromo, mCongestion;
            private EatItem mEatItem;

            public ViewHolder(final View itemView)
            {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.li_eh_image);
                mHeader = (TextView) itemView.findViewById(R.id.li_eh_header);
                mWifistatus = (TextView) itemView.findViewById(R.id.li_eh_wifistatus);
                mPromo = (TextView) itemView.findViewById(R.id.li_eh_promo);
                mCongestion = (TextView) itemView.findViewById(R.id.li_eh_congestion);

            }

            public static int getLayoutRes()
            {
                return LAYOUT_RES;
            }

            public void bindData(EatItem eatItem, Context context)
            {
                mEatItem = eatItem;
                Glide.with(context).load(eatItem.getImageUri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView).onLoadFailed(new IllegalStateException("FAIL LOAD"), context.getResources().getDrawable(R.drawable.abc_btn_rating_star_on_mtrl_alpha));

                mHeader.setText(eatItem.getHeader());
                mWifistatus.setText(eatItem.getWifiStatus());
                if (eatItem.isWifiAvailable())
                {
                    mWifistatus.setTextColor(context.getResources().getColor(R.color.textColorPositive));
                }
                else
                {
                    mWifistatus.setTextColor(context.getResources().getColor(R.color.textColorNegative));
                }

                mPromo.setText(eatItem.getPromo());
                mCongestion.setText(eatItem.getCongestion());
            }

            public void startTimer()
            {
                AsyncTask<Integer, Integer, Void> asyncTask = new AsyncTask<Integer, Integer, Void>()
                {
                    private String basePromo = "{init}";

                    @Override
                    protected Void doInBackground(final Integer... params)
                    {
                        int time = params[0];

                        try
                        {
                            if (time > 0)
                            {
                                while (time > 0)
                                {
                                    time--;
                                    publishProgress(time);
                                    Thread.sleep(1000);
                                }
                            }

                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(final Integer... values)
                    {
                        super.onProgressUpdate(values);
                        if (mEatItem != null)
                        {

                            if (basePromo.equals("{init}"))
                            {
                                basePromo = mEatItem.getPromo();
                            }

                            String outPromo = basePromo + "Ending in " + values[0] + " minutes";

                            mEatItem.setPromo(outPromo);
                        }
                    }
                };
                asyncTask.execute(mEatItem.getTimedPromo());

            }
        }
    }

}
