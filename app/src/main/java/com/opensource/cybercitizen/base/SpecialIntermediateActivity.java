package com.opensource.cybercitizen.base;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.opensource.common.ui.BaseDrawerActivity;
import com.opensource.cybercitizen.R;

public abstract class SpecialIntermediateActivity extends BaseDrawerActivity
{
    private static final int HEADER_IMG_RES = R.raw.city_nav;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Glide.with(SpecialIntermediateActivity.this).load(HEADER_IMG_RES).into(getNavHeaderImageView());
    }
}
