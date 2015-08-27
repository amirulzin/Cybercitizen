package com.opensource.cybercitizen.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.opensource.common.ui.BaseDrawerActivity;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.UserActivity;

public abstract class SpecialIntermediateActivity extends BaseDrawerActivity
{
    private static final int HEADER_IMG_RES = R.raw.city_nav;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Glide.with(SpecialIntermediateActivity.this).load(HEADER_IMG_RES).into(getNavHeaderImageView());
        getNavigationView().findViewById(R.id.lnh_headerfab_container).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                startActivity(new Intent(SpecialIntermediateActivity.this, UserActivity.class));
                getDrawerLayout().closeDrawers();
            }
        });

    }
}
