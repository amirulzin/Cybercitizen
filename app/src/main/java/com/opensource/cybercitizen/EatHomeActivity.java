package com.opensource.cybercitizen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opensource.common.ui.BaseDrawerActivity;

import java.util.ArrayList;
import java.util.List;

public class EatHomeActivity extends BaseDrawerActivity
{
    private final List<EatItem> mEatItems = new ArrayList<>(128);
    private RecyclerView.Adapter<EatItem.ViewHolder> mRecyclerView = new RecyclerView.Adapter<EatItem.ViewHolder>()
    {
        @Override
        public EatItem.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
        {
            return new EatItem.ViewHolder(LayoutInflater.from(EatHomeActivity.this).inflate(EatItem.ViewHolder.getLayoutRes(), parent, false));
        }

        @Override
        public void onBindViewHolder(final EatItem.ViewHolder holder, final int position)
        {
            holder.bindData(mEatItems.get(position), EatHomeActivity.this);
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

    @Override
    public int getDrawerItemId()
    {
        return R.menu.menu_drawer;
    }

    public static class EatItem
    {
        String imageUri, header, wifistatus, congestion, promo;

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

        public String getPromo()
        {
            return promo;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            private static final int LAYOUT_RES = R.layout.listitem_eathome;
            private ImageView mImageView;
            private TextView mHeader, mWifistatus, mPromo, mCongestion;

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
                Glide.with(context).load(eatItem.getImageUri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);

                mHeader.setText(eatItem.getHeader());
                mWifistatus.setText(eatItem.getWifiStatus());
                mPromo.setText(eatItem.getPromo());
                mCongestion.setText(eatItem.getCongestion());
            }
        }
    }

}
