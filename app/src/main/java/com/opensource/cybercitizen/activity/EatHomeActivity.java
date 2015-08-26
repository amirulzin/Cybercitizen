package com.opensource.cybercitizen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.model.EatItem;
import com.opensource.cybercitizen.base.RecyclerHomeBaseActivity;
import com.opensource.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class EatHomeActivity extends RecyclerHomeBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private final List<EatItem> mEatItems = new ArrayList<>();
    private final ArrayMap<EatItem, List<String>> mEatItemSearchMap = new ArrayMap<>(16);
    private final String ACTIVITY_MAIN_TITLE = "Eat";
    GoogleApiClient mGoogleApiClient;

    Location mCurrentLocation;
    AtomicBoolean displayed = new AtomicBoolean(false);
    private RecyclerView.Adapter mRecyclerAdapter = new RecyclerView.Adapter()
    {
        private static final int TOP_HEADER = 0;
        private static final int ITEMS = 1;
        final List<EatItem> mFilteredItems = new ArrayList<>(6);
        private boolean isFiltered = false;
        private final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(final String query)
            {

                if (query != null)
                {
                    String searchQuery = query.trim();
                    if (searchQuery.length() > 0)
                    {
                        isFiltered = true;
                        notifyItemRangeRemoved(1, mEatItems.size() + 1);
                        mFilteredItems.clear();
                        mFilteredItems.addAll(search(searchQuery, false));
                        notifyItemRangeInserted(1, mFilteredItems.size() + 1);
                    }
                    else
                    {
                        isFiltered = false;
                        notifyItemRangeRemoved(1, mFilteredItems.size() + 1);
                        notifyItemRangeInserted(1, mEatItems.size() + 1);
                        mFilteredItems.clear();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText)
            {
                if (newText != null)
                {
                    String searchQuery = newText.trim();
                    if (searchQuery.length() > 0)
                    {
                        isFiltered = true;
                        notifyItemRangeRemoved(1, mEatItems.size() + 1);
                        mFilteredItems.clear();
                        mFilteredItems.addAll(search(newText, true));
                        notifyItemRangeInserted(1, mFilteredItems.size() + 1);
                    }
                    else
                    {
                        isFiltered = false;
                        notifyItemRangeRemoved(1, mFilteredItems.size() + 1);
                        notifyItemRangeInserted(1, mEatItems.size() + 1);
                        mFilteredItems.clear();
                    }
                }
                return false;
            }
        };
        private final SearchView.OnCloseListener mOnCloseListener = new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                mFilteredItems.clear();
                isFiltered = false;
                return false;
            }
        };

        private List<EatItem> search(String query, boolean singleLetterUpdate)
        {
            List<EatItem> out = new ArrayList<>();
            for (int i = 0; i < mEatItemSearchMap.size(); i++)
            {
                List<String> searchTags = mEatItemSearchMap.valueAt(0);
                for (String searchTag : searchTags)
                {
                    if (searchTag.equalsIgnoreCase(query))
                    {
                        out.add(mEatItemSearchMap.keyAt(i));
                        break;
                    }
                    if (singleLetterUpdate)
                    {
                        if (searchTag.matches(".*" + query + ".*"))
                        {
                            out.add(mEatItemSearchMap.keyAt(i));
                            break;
                        }
                    }
                }
            }

            return out;
        }
        @Override
        public int getItemViewType(final int position)
        {
            if (position == 0)
            {
                return TOP_HEADER;
            }
            return ITEMS;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
        {
            final LayoutInflater layoutInflater = LayoutInflater.from(EatHomeActivity.this);
            if (viewType == ITEMS)
            {
                return new EatItem.EatItemViewHolder(layoutInflater.inflate(EatItem.EatItemViewHolder.getLayoutRes(), parent, false));
            }
            else if (viewType == TOP_HEADER)
            {
                return new HeaderSearchHolder(layoutInflater.inflate(HeaderSearchHolder.getLayoutRes(), parent, false));
            }
            else return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
        {
            if (holder != null)
            {
                if (holder instanceof EatItem.EatItemViewHolder && position > 0)
                {
                    EatItem eatItem = null;
                    int offsetPos = position - 1;
                    if (isFiltered)
                    {
                        if (mFilteredItems.size() > 0)
                        {
                            eatItem = mFilteredItems.get(offsetPos);
                        }
                    }
                    else
                    {
                        eatItem = mEatItems.get(offsetPos);
                    }

                    if (eatItem != null)
                    {
                        EatItem.EatItemViewHolder viewHolder = (EatItem.EatItemViewHolder) holder;
                        final EatItem outEat = eatItem;
                        viewHolder.setOnClickCallback(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(final View v)
                            {
                                launchDialog(outEat, getContentContainer());
                            }
                        });

                        viewHolder.bindData(eatItem, EatHomeActivity.this);

                        if (mGoogleApiClient.isConnected() && mCurrentLocation != null)
                        {
                            viewHolder.displayDistanceFrom(eatItem, mCurrentLocation);
                        }
                    }
                }
                else if (holder instanceof HeaderSearchHolder)
                {
                    final HeaderSearchHolder viewHolder = (HeaderSearchHolder) holder;

                    viewHolder.bindData(onQueryTextListener, mOnCloseListener);
                }
            }
        }

        @Override
        public int getItemCount()
        {
            return mEatItems.size() + 1;
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(EatHomeActivity.this).addApi(LocationServices.API).addOnConnectionFailedListener(this).addConnectionCallbacks(this).build();
        mGoogleApiClient.connect();
    }

    public void setupList()
    {
        final EatItem secretRecipe = new EatItem("http://i.imgur.com/LWNkMtS.png", "Secret Recipe").setCongestion(42).setWifiAvailable(false).setLatLng(new LatLng(2.9232235, 101.6535586));

        final EatItem matAyam = new EatItem("http://i.imgur.com/M94VSrh.png", "Restoran Mat Ayam Kampung").setCongestion(23).setWifiAvailable(false).setLatLng(new LatLng(2.9173249, 101.6501049)).setPromo("Lunch hour promotion\nEnding in 2 hours");

        final EatItem subway = new EatItem("http://i.imgur.com/GlNronD.png", "Subway").setCongestion(19).setWifiAvailable(true).setLatLng(new LatLng(2.9236703, 101.6613761));
        final EatItem starbucks = new EatItem("http://i.imgur.com/ydXZcaU.png", "Starbucks").setCongestion(45).setWifiAvailable(true);
        starbucks.setLatLng(new LatLng(2.9212027, 101.655918));

        final EatItem kfc = new EatItem("http://i.imgur.com/qSBRJw3.png", "Kentucky Fried Chicken").setCongestion(75).setWifiAvailable(true);
        kfc.setLatLng(new LatLng(2.9207093, 101.659172));

        final EatItem mcD = new EatItem("http://i.imgur.com/7GaUg0d.png", "McDonalds").setCongestion(11).setWifiAvailable(true);

        mcD.setLatLng(new LatLng(2.9750013, 101.6717934));

        final EatItem pizzaHut = new EatItem("http://i.imgur.com/NpzwdWP.png", "Pizza Hut").setCongestion(10).setWifiAvailable(false);
        pizzaHut.setLatLng(new LatLng(2.920422, 101.6591227));

        mEatItems.addAll(Arrays.asList(subway, kfc, pizzaHut, secretRecipe, starbucks, matAyam));

        for (EatItem eatItem : mEatItems)
        {
            List<String> searchStrings = new ArrayList<>();
            searchStrings.add(eatItem.getHeader());
            searchStrings.addAll(eatItem.getTags());
            mEatItemSearchMap.put(eatItem, searchStrings);
        }
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
        setDrawerNavigationButton(getTitleToolbar());

        Util.postOnPreDraw(recyclerView, new Runnable()
        {
            @Override
            public void run()
            {
                displayed.set(true);
            }
        });

    }

    @Override
    public int getDrawerItemId()
    {
        return R.id.md_nav_eat;
    }

    public void launchDialog(EatItem eatItem, ViewGroup parent)
    {
        startActivityForResult(EatDialogActivity.getDialogIntent(this, eatItem, mCurrentLocation), 0);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
//            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
        {
            Snackbar.make(getRecyclerView(), "Thank you for your patronage. Here's a redeemable 10% off voucher code", Snackbar.LENGTH_INDEFINITE).setAction("REDEEM", new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    final Set<String> strings = new ArraySet<>(64);
                    final UUID key = UUID.randomUUID();

                    EatHomeActivity.this.getSharedPreferences("shared_prefs", MODE_PRIVATE).getStringSet("DISCOUNT_COUPON", strings).add(key.toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(EatHomeActivity.this).setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which)
                        {

                            EatHomeActivity.this.getSharedPreferences("shared_prefs", MODE_PRIVATE).edit().putStringSet("DISCOUNT_COUPON", strings).apply();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("SKIP", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    builder.setMessage("COUPON: CYBERJAYAFAIR15");
                    builder.show();
                }
            }).show();
        }

    }

    @Override
    public void onConnected(final Bundle bundle)
    {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        LocationRequest locationRequest = new LocationRequest().setInterval(60 * 1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        if (displayed.get() && mCurrentLocation != null)
        {
            onLocationChanged(mCurrentLocation);
        }
    }

    @Override
    public void onConnectionSuspended(final int i)
    {

    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(final Location location)
    {
        sortToNearest(location);
        mRecyclerAdapter.notifyItemRangeChanged(1, mEatItems.size() + 1);
    }

    private void sortToNearest(final Location location)
    {
        final Comparator<EatItem> comparator = new Comparator<EatItem>()
        {
            @Override
            public int compare(final EatItem lhs, final EatItem rhs)
            {
                return (int) (lhs.getLocation().distanceTo(location) - rhs.getLocation().distanceTo(location));
            }
        };
        Collections.sort(mEatItems, comparator);
    }

    private static class HeaderSearchHolder extends RecyclerView.ViewHolder
    {

        private final SearchView mSearchView;

        public HeaderSearchHolder(final View itemView)
        {
            super(itemView);
            mSearchView = (SearchView) itemView.findViewById(R.id.lhs_searchview);
        }

        public static int getLayoutRes()
        {
            return R.layout.layout_header_search;
        }

        public void bindData(SearchView.OnQueryTextListener listener, SearchView.OnCloseListener onCloseListener)
        {
            mSearchView.setOnQueryTextListener(listener);
            mSearchView.setOnCloseListener(onCloseListener);
        }
    }
}