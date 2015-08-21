package com.opensource.common.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.opensource.cybercitizen.R;

public abstract class BaseDrawerActivity extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS | Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_drawer);
        setupViews(savedInstanceState);
    }

    private void setupViews(final Bundle savedInstanceState)
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.lbd_drawerlayout);
        mFrameLayout = (FrameLayout) findViewById(R.id.lbd_contentcontainer);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.lbd_navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                int itemId = menuItem.getItemId();
                if (itemId != getDrawerItemId())
                {
                    ActivityFactory.changeActivityFromDrawer(BaseDrawerActivity.this, itemId);
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    /**
     * For concrete implementation return an ID here that corresponds with the navigation item menu id that was set in the NavigationView menu resource.
     */
    public abstract int getDrawerItemId();

    public View inflateContent(@LayoutRes int layoutResourceId)
    {
        View inflatedView = LayoutInflater.from(BaseDrawerActivity.this).inflate(layoutResourceId, getContentContainer(), false);
        getContentContainer().addView(inflatedView);
        return inflatedView;
    }

    public FrameLayout getContentContainer()
    {
        return mFrameLayout;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    public DrawerLayout getDrawerLayout()
    {
        return mDrawerLayout;
    }

    public void setDrawerNavigationButton(@NonNull Toolbar toolbar)
    {
        if (mDrawerLayout != null)
        {
            mDrawerToggle = new ActionBarDrawerToggle(BaseDrawerActivity.this, mDrawerLayout, toolbar, R.string.acc_drawer_open, R.string.acc_drawer_close);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
    }


    /**
     * A Fragment who optionally depends on {@link BaseDrawerActivity} for communicating calls.
     */
    public abstract static class DrawerFragment extends Fragment
    {

        public void setDrawerNavigationButton(@NonNull Toolbar toolbar)
        {
            final FragmentActivity activity = getActivity();
            if (activity != null && activity instanceof BaseDrawerActivity)
            {
                ((BaseDrawerActivity) activity).setDrawerNavigationButton(toolbar);

            }
        }

        public String getFragmentTag()
        {
            return getClass().getSimpleName();
        }
    }
}
