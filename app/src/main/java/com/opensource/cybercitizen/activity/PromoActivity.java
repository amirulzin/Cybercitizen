package com.opensource.cybercitizen.activity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.base.NestedScrollHomeBaseActivity;

public class PromoActivity extends NestedScrollHomeBaseActivity
{
    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {
        inflateNestedContent(R.layout.activity_promo);
        setDrawerNavigationButton(getTitleToolbar());
        getCollapsingToolbarLayout().setTitle("Saved Promotions");
        Glide.with(this).load(R.raw.header_promo).into(getHeaderImageView());
    }

    @Override
    public int getDrawerItemId()
    {
        return R.id.md_nav_promo;
    }
}
