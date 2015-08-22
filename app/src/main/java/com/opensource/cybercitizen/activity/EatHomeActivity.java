package com.opensource.cybercitizen.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.base.RecyclerHomeBaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EatHomeActivity extends RecyclerHomeBaseActivity
{
    private final List<EatItem> mEatItems = new ArrayList<>();
    private final String ACTIVITY_MAIN_TITLE = "Eat";

    private RecyclerView.Adapter<EatItem.EatItemViewHolder> mRecyclerAdapter = new RecyclerView.Adapter<EatItem.EatItemViewHolder>()
    {
        @Override
        public EatItem.EatItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
        {
            final View itemView = LayoutInflater.from(EatHomeActivity.this).inflate(EatItem.EatItemViewHolder.getLayoutRes(), parent, false);
            return new EatItem.EatItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final EatItem.EatItemViewHolder holder, final int position)
        {

            final EatItem eatItem = mEatItems.get(position);


            holder.setOnClickCallback(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    launchDialog(eatItem, getContentContainer());

                }
            });
//            if (holder instanceof EatItem.EatItemViewHolder.ExpandedView)
//            {
//                final EatItem.EatItemViewHolder.ExpandedView expandedHolder = (EatItem.EatItemViewHolder.ExpandedView) holder;
//
//                expandedHolder.setOnClickCallback(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(final View v)
//                    {
//                        Log.v("EXPANDED", "POS" + position);
//                        if (v.getId() == R.id.li_eh_baseview)
//                        {
//                            eatItem.setExpanded(false);
//                            notifyItemChanged(position);
//                        }
//
//                    }
//                });
//
//                expandedHolder.bindData(eatItem, EatHomeActivity.this);
//            }
//            else
//            {
//
//
//            }
            holder.bindData(eatItem, EatHomeActivity.this);

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

        mEatItems.addAll(Arrays.asList(kfc, starbucks, mcD, pizzaHut));

//        mEatItems.put(kfc, kfc.getExpanded());
//        mEatItems.put(mcD, mcD.getExpanded());
//        mEatItems.put(starbucks, starbucks.getExpanded());
//        mEatItems.put(pizzaHut, pizzaHut.getExpanded());
    }


    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        setupList();
        getCollapsingToolbarLayout().setTitle(ACTIVITY_MAIN_TITLE);
        final RecyclerView recyclerView = getRecyclerView();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EatHomeActivity.this, LinearLayoutManager.VERTICAL, false));

        Glide.with(this).load(R.raw.eat_header).into(getHeaderImageView());
        final Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            drawable = getResources().getDrawable(R.drawable.gradient_shadow_top);
        }
        else
            drawable = getResources().getDrawable(R.drawable.gradient_shadow_top, getTheme());

        getCollapsingToolbarLayout().setForeground(drawable);

    }

    @Override
    public int getDrawerItemId()
    {
        return R.menu.menu_drawer;
    }

    public void launchDialog(EatItem eatItem, ViewGroup parent)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_eathome_expanded, parent, false);
        EatItem.ExpandedView expandedView = new EatItem.ExpandedView(view);
        expandedView.bindData(eatItem, this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view).setPositiveButton("I'M COMING", null).setNeutralButton("DISMISS", null);
        builder.show();
    }

    public static class EatItem
    {

        String imageUri;
        String header;
        String wifistatus;
        String congestion;
        String promo;
        int timedPromo = -1;
        boolean expanded = false;
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

        public boolean isExpanded()
        {
            return this.expanded;
        }

        public void setExpanded(boolean expanded)
        {
            this.expanded = expanded;
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

        public static class EatItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
//            public static final int VIEWTYPE_COMPACT = 0;
//            public static final int VIEWTYPE_EXPANDED = 1;
//            public int getExpanded()
//            {
//                if (expanded)
//                    return VIEWTYPE_EXPANDED;
//                else return VIEWTYPE_COMPACT;
//            }

            private static final int LAYOUT_RES = R.layout.listitem_eathome;
            private ImageView mImageView;
            private TextView mHeader, mWifistatus, mPromo, mCongestion;
            private EatItem mEatItem;
            private View.OnClickListener onClickCallback;

            public EatItemViewHolder(final View itemView)
            {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.li_eh_image);
                mHeader = (TextView) itemView.findViewById(R.id.li_eh_header);
                mWifistatus = (TextView) itemView.findViewById(R.id.li_eh_wifistatus);
                mPromo = (TextView) itemView.findViewById(R.id.li_eh_promo);
                mCongestion = (TextView) itemView.findViewById(R.id.li_eh_congestion);

                itemView.setOnClickListener(this);
            }

            public static int getLayoutRes()
            {
                return LAYOUT_RES;
            }

            public void setOnClickCallback(final View.OnClickListener onClickCallback)
            {
                this.onClickCallback = onClickCallback;
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

            @Override
            public void onClick(final View v)
            {
                if (onClickCallback != null)
                    onClickCallback.onClick(v);
            }
        }

        public static class ExpandedView extends EatItemViewHolder
        {
            ImageButton walk, car, bus, taxi;
            TextView tags;

            public ExpandedView(final View itemView)
            {
                super(itemView);
                walk = (ImageButton) itemView.findViewById(R.id.li_ehe_walk);
                car = (ImageButton) itemView.findViewById(R.id.li_ehe_car);
                bus = (ImageButton) itemView.findViewById(R.id.li_ehe_bus);
                taxi = (ImageButton) itemView.findViewById(R.id.li_ehe_taxi);
                tags = (TextView) itemView.findViewById(R.id.li_ehe_tags);
            }

            @Override
            public void bindData(final EatItem eatItem, final Context context)
            {
                super.bindData(eatItem, context);

                taxi.setOnClickListener(ExpandedView.this);
                walk.setOnClickListener(ExpandedView.this);
                car.setOnClickListener(ExpandedView.this);
                bus.setOnClickListener(ExpandedView.this);
            }

        }
    }

}