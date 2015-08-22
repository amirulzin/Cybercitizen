package com.opensource.cybercitizen;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.opensource.common.ui.BaseDrawerActivity;

public abstract class HomeBaseActivity extends BaseDrawerActivity
{
    private RecyclerView mRecyclerView;
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
        final View view = inflateContent(R.layout.layout_base_nested_collapsible);
        innerSetupViews(view);
        setupView(savedInstanceState, view);
    }

    public abstract void setupView(Bundle savedInstanceState, View baseLayout);

    public RecyclerView getRecyclerView()
    {
        return mRecyclerView;
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
        mRecyclerView = (RecyclerView) baseLayout.findViewById(R.id.lbnc_recyclerview);

    }

}
