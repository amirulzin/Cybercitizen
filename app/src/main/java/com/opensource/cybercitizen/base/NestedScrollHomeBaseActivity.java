package com.opensource.cybercitizen.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.opensource.common.ui.BaseDrawerActivity;
import com.opensource.cybercitizen.R;

public abstract class NestedScrollHomeBaseActivity extends BaseDrawerActivity
{
    private NestedScrollView mNestedScrollView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mHeaderImageView;
    private Toolbar mTitleToolbar;

    public Toolbar getTitleToolbar()
    {
        return mTitleToolbar;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final View view = inflateContent(R.layout.layout_base_recycler_collapsible);
        innerSetupViews(view);
        setupView(savedInstanceState, view);
    }

    public abstract void setupView(Bundle savedInstanceState, View baseLayout);

    public NestedScrollView getRecyclerView()
    {
        return mNestedScrollView;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout()
    {
        return mCollapsingToolbarLayout;
    }

    public ImageView getHeaderImageView()
    {
        return mHeaderImageView;
    }

    private void innerSetupViews(View baseLayout)
    {
        mTitleToolbar = (Toolbar) baseLayout.findViewById(R.id.lbnc_toolbar);
        mHeaderImageView = (ImageView) baseLayout.findViewById(R.id.lbnc_headerimage);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) baseLayout.findViewById(R.id.lbnc_collapsingtoolbar);
        mNestedScrollView = (NestedScrollView) baseLayout.findViewById(R.id.lbnc_scrollview);

    }

    public View inflateNestedContent(@LayoutRes int layoutResourceId)
    {
        View inflatedView = LayoutInflater.from(NestedScrollHomeBaseActivity.this).inflate(layoutResourceId, mNestedScrollView, false);
        mNestedScrollView.addView(inflatedView);
        return inflatedView;
    }
}
