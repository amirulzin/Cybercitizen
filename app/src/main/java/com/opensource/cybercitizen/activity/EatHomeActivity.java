package com.opensource.cybercitizen.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.model.EatItem;
import com.opensource.cybercitizen.base.RecyclerHomeBaseActivity;
import com.opensource.cybercitizen.fragment.EatDialogActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EatHomeActivity extends RecyclerHomeBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
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
            if (eatItem.getTimedPromo() > 0)
                holder.startTimer();

        }

        @Override
        public int getItemCount()
        {
            return mEatItems.size();
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void setupList()
    {
        final EatItem starbucks = new EatItem("http://i.imgur.com/ydXZcaU.png", "Starbucks").setCongestion(50).setWifiAvailable(true);
        starbucks.setLatLng(new LatLng(2.9212027, 101.655918));

        final EatItem kfc = new EatItem("http://i.imgur.com/qSBRJw3.png", "Kentucky Fried Chicken").setCongestion(90).setWifiAvailable(true);
        kfc.setLatLng(new LatLng(2.9207093, 101.659172));

        final EatItem mcD = new EatItem("http://i.imgur.com/7GaUg0d.png", "McDonalds").setCongestion(10).setWifiAvailable(true).setPromo("Lunch hour promotion\nEnding in 27 minutes");

        mcD.setLatLng(new LatLng(2.9750013, 101.6717934));

        final EatItem pizzaHut = new EatItem("http://i.imgur.com/NpzwdWP.png", "Pizza Hut").setCongestion(10).setWifiAvailable(false);
        pizzaHut.setLatLng(new LatLng(2.920422, 101.6591227));

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
        startActivityForResult(new Intent(this, EatDialogActivity.class).putExtra(EatDialogActivity.getEatItemIntentKey(), eatItem), 0);
    }

    @Override
    public void onConnected(final Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(final int i)
    {

    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult)
    {

    }

}