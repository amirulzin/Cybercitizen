package com.opensource.cybercitizen.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opensource.common.ui.GridItemDecoration;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.base.RecyclerHomeBaseActivity;
import com.opensource.cybercitizen.controller.Weather;
import com.opensource.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends RecyclerHomeBaseActivity
{
    private final List<HomeListItem> mHomeListItems = new ArrayList<>(16);
    private RecyclerView.Adapter<HomeListItem.ViewHolder> mRecyclerViewAdapter = new RecyclerView.Adapter<HomeListItem.ViewHolder>()
    {

        @Override
        public HomeListItem.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
        {
            return new HomeListItem.ViewHolder(LayoutInflater.from(HomeActivity.this).inflate(HomeListItem.ViewHolder.getLayoutRes(), parent, false));
        }

        @Override
        public void onBindViewHolder(final HomeListItem.ViewHolder holder, final int position)
        {
            holder.bindData(mHomeListItems.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mHomeListItems.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init()
    {
        Weather weather = new Weather();
        weather.getWeather(new Weather.Listener()
        {
            @Override
            public void onWeatherReady(final Weather weather)
            {
                getHeaderImageView().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int gifId;
                        if (weather.isSunnyCurrently())
                        {
                            gifId = R.raw.sunny;
                        }
                        else
                            gifId = R.raw.rainy;
                        Glide.with(HomeActivity.this).load(gifId).into(getHeaderImageView());
                    }
                });
            }
        });
    }


    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        setupItems(mHomeListItems);
        Toolbar toolbar = (Toolbar) findViewById(R.id.lbnc_toolbar);
        if (toolbar != null)
        {
            setDrawerNavigationButton(toolbar);
        }
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) baseLayout.findViewById(R.id.lbnc_collapsingtoolbar);
        if (collapsingToolbar != null)
        {
            collapsingToolbar.setTitle("Cybercitizen");
        }

        RecyclerView recyclerView = (RecyclerView) baseLayout.findViewById(R.id.lbnc_recyclerview);
        if (recyclerView != null)
        {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(mRecyclerViewAdapter);
            recyclerView.setClipToPadding(false);
            int paddingPx = Util.dpToPx(HomeActivity.this, 4);
            recyclerView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
            recyclerView.addItemDecoration(new GridItemDecoration(HomeActivity.this, 2, 4));

        }

        Glide.with(this).load(R.raw.sunny).into(getHeaderImageView());
        final Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            drawable = getResources().getDrawable(R.drawable.gradient_shadow_top);
        }
        else
            drawable = getResources().getDrawable(R.drawable.gradient_shadow_top, getTheme());

        getCollapsingToolbarLayout().setForeground(drawable);
    }

    private void setupItems(final List<HomeListItem> homeListItems)
    {
        homeListItems.add(new HomeListItem("Shop", 25, getResources().getColor(R.color.teal_A200)));
        final HomeListItem eat = new HomeListItem("Eat", 12, getResources().getColor(R.color.indigo_A200));
        eat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                startActivity(new Intent(HomeActivity.this, EatHomeActivity.class));
            }
        });
        homeListItems.add(eat);
        homeListItems.add(new HomeListItem("Health", 3, getResources().getColor(R.color.light_green_A200)));

        homeListItems.add(new HomeListItem("Travel", 8, getResources().getColor(R.color.orange_A200)));
        homeListItems.add(new HomeListItem("Outdoor", 16, getResources().getColor(R.color.pink_A200)));
        homeListItems.add(new HomeListItem("Exhibition", 14, getResources().getColor(R.color.red_A200)));
    }

    @Override
    public int getDrawerItemId()
    {
        return R.id.md_nav_home;
    }

    public static final class HomeListItem
    {
        private String title;
        private int itemCounts;
        private int color;
        private View.OnClickListener mOnClickListener;

        public HomeListItem(@NonNull final String title, final int itemCounts, @ColorInt final int color)
        {
            this.title = title;
            this.itemCounts = itemCounts;
            this.color = color;
        }

        public String getTitle()
        {
            return title;
        }

        public HomeListItem setTitle(final String title)
        {
            this.title = title;
            return this;
        }

        public int getItemCounts()
        {
            return itemCounts;
        }

        public HomeListItem setItemCounts(final int itemCounts)
        {
            this.itemCounts = itemCounts;
            return this;
        }

        public int getColor()
        {
            return color;
        }

        public HomeListItem setColor(final int color)
        {
            this.color = color;
            return this;
        }


        public View.OnClickListener getOnClickListener()
        {
            return mOnClickListener;
        }

        public HomeListItem setOnClickListener(View.OnClickListener onClickListener)
        {
            mOnClickListener = onClickListener;
            return this;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            private static final int VIEWHOLDER_TAG = 1942;
            private static final int LAYOUT_RES = R.layout.listitem_mainhome;
            private final TextView mTitle;
            private final TextView mItemCount;
            private final View mColorView;
            private final View mBaseView;
            private View.OnClickListener mOnClickListener;


            public ViewHolder(final View itemView)
            {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.li_h_title);
                mItemCount = (TextView) itemView.findViewById(R.id.li_h_itemscount);
                mColorView = itemView.findViewById(R.id.li_h_barcolor);
                mBaseView = itemView.findViewById(R.id.li_h_baselayout);

                itemView.setOnClickListener(this);
            }

            public static int getLayoutRes()
            {
                return LAYOUT_RES;
            }

            public static int getTag()
            {
                return VIEWHOLDER_TAG;
            }

            public void bindData(HomeListItem homeListItem)
            {
                mTitle.setText(homeListItem.getTitle());
                mItemCount.setText(homeListItem.getItemCounts() + " items");
                mColorView.setBackgroundColor(homeListItem.getColor());
                if (homeListItem.getOnClickListener() != null)
                {
                    mOnClickListener = homeListItem.getOnClickListener();
                }
            }

            @Override
            public void onClick(final View v)
            {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(v);
            }
        }

    }
}
