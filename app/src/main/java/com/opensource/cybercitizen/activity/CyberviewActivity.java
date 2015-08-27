package com.opensource.cybercitizen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.base.NestedScrollHomeBaseActivity;

public class CyberviewActivity extends NestedScrollHomeBaseActivity
{
    @Override
    public void setupView(final Bundle savedInstanceState, final View baseLayout)
    {

        getCollapsingToolbarLayout().setTitle("CyberView Admin");
        View baseParent = inflateNestedContent(R.layout.activity_cyberview);
        final TabLayout tabLayout = (TabLayout) baseParent.findViewById(R.id.ac_tab);
        final ViewPager viewPager = (ViewPager) baseParent.findViewById(R.id.ac_viewpager);

//        TabLayout.Tab merchantTab = tabLayout.newTab().setText("Merchant");
//        tabLayout.addTab(merchantTab, true);
//        TabLayout.Tab userTab = tabLayout.newTab().setText("Citizens");
//        tabLayout.addTab(userTab);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorControlActivated));
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(final int position)
            {
                if (position == 0)
                {
                    return new ProtoMerchantListFragment();
                }
                if (position == 1)
                    return new ProtoUserReports();
                return null;
            }

            @Override
            public CharSequence getPageTitle(final int position)
            {
                if (position == 0)
                {
                    return "Merchants";
                }
                if (position == 1)
                    return "Citizens";
                else
                    return super.getPageTitle(position);
            }

            @Override

            public int getCount()
            {
                return 2;
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getHeaderImageView().setImageDrawable(getResources().getDrawable(R.drawable.cyberview));
    }

    @Override
    public int getDrawerItemId()
    {
        return 0;
    }

    public static class ProtoMerchantListFragment extends Fragment
    {
        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.proto_fragment_cyberview_merchants, container, false);
        }

        @Override
        public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.proto_cv_merchant).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    onMcDonaldsProtoClick(v);
                }
            });
        }

        public void onMcDonaldsProtoClick(final View view)
        {
            final String[] issues = new String[]{
                    "- Occasional power malfunctions", "- Unclean alleys"
            };
            new AlertDialog.Builder(getActivity()).setTitle("Outstanding Issues").setItems(issues, null).setPositiveButton("Schedule Review", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    Toast.makeText(getActivity(), "Review scheduled and synced with server", Toast.LENGTH_LONG).show();
                    final TextView tv = (TextView) view.findViewById(R.id.proto_action_text);
                    tv.setText("Review scheduled");
                    tv.setTextColor(getResources().getColor(R.color.textColorNegative));
                    dialog.dismiss();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    public static class ProtoUserReports extends Fragment
    {
        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.proto_fragment_citizenreports, container, false);
        }

        @Override
        public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}
