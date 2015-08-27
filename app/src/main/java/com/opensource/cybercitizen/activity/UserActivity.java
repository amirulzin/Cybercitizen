package com.opensource.cybercitizen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.base.NestedScrollHomeBaseActivity;

public class UserActivity extends NestedScrollHomeBaseActivity
{
    public static final int ACTIVITY_ID = -1918;

    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        inflateNestedContent(R.layout.activity_userprofile);
        getCollapsingToolbarLayout().setTitle("James Hall");
        setDrawerNavigationButton(getTitleToolbar());
        getHeaderImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(R.raw.merchant_main).into(getHeaderImageView());
    }

    @Override
    public int getDrawerItemId()
    {
        return ACTIVITY_ID;
    }
}
