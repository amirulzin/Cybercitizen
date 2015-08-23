package com.opensource.common.ui;

import android.content.Context;
import android.content.Intent;

import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.EatHomeActivity;
import com.opensource.cybercitizen.activity.HomeActivity;

public final class ActivityFactory
{
    public static void changeActivityFromDrawer(Context context, int navId)
    {
        Intent intent = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (navId)
        {
            case R.id.md_nav_home:
                intent.setClass(context, HomeActivity.class);
                break;
            case R.id.md_nav_eathome:
                intent.setClass(context, EatHomeActivity.class);
                break;
            case R.id.md_nav_about:
                intent = null;
        }

        if (intent != null)
            context.startActivity(intent);
    }
}