package com.opensource.cybercitizen;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opensource.common.ui.BaseDrawerActivity;
import com.opensource.common.ui.GridItemSetting;
import com.opensource.common.ui.MarginItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseDrawerActivity
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
        View parent = inflateContent(R.layout.layout_base_nested_collapsible);
        setupItems(mHomeListItems);
        setupView(parent, savedInstanceState);
    }

    private void setupView(final View baseLayout, final Bundle savedInstanceState)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.lbnc_toolbar);
        if (toolbar != null)
        {
            setDrawerNavigationButton(toolbar);
        }
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) baseLayout.findViewById(R.id.lbnc_collapsingtoolbar);
        if (collapsingToolbar != null)
        {
            collapsingToolbar.setTitle("What I want to do today");
        }

        RecyclerView recyclerView = (RecyclerView) baseLayout.findViewById(R.id.lbnc_recyclerview);
        if (recyclerView != null)
        {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2, LinearLayoutManager.VERTICAL, false);
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            GridItemSetting gridItemSetting = new GridItemSetting(displayMetrics.density, displayMetrics.widthPixels, 160, 1f, 0);
            recyclerView.addItemDecoration(new MarginItemDecoration(0, gridItemSetting.getColumnCount()));
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(mRecyclerViewAdapter);

        }
    }

    private void setupItems(final List<HomeListItem> homeListItems)
    {
        homeListItems.add(new HomeListItem("Shop", 25, getResources().getColor(R.color.teal_A200)));
        homeListItems.add(new HomeListItem("Eat", 12, getResources().getColor(R.color.indigo_A200)));
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

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            private static final int VIEWHOLDER_TAG = 1942;
            private static final int LAYOUT_RES = R.layout.listitem_home;

            private final TextView mTitle;
            private final TextView mItemCount;
            private final View mColorView;

            public ViewHolder(final View itemView)
            {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.li_h_title);
                mItemCount = (TextView) itemView.findViewById(R.id.li_h_itemscount);
                mColorView = itemView.findViewById(R.id.li_h_barcolor);
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
                    ((View) mTitle.getParent()).setOnClickListener(homeListItem.getOnClickListener());
                }
            }
        }

    }
}
