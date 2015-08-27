package com.opensource.cybercitizen.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.model.DTSBus;
import com.opensource.cybercitizen.activity.model.EatItem;
import com.opensource.cybercitizen.controller.Weather;
import com.opensource.util.Util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class EatDialogActivity extends AppCompatActivity implements OnMapReadyCallback, Weather.Listener
{

    private SupportMapFragment mMapFragment;
    private TextView mSuggestion;
    private Weather weatherData;
    private EatItem mEatItem;
    private ImageView mImageView;
    private TextView mHeader, mWifistatus, mPromo, mCongestion;
    private ImageButton walk, car, bus, taxi;
    private TextView tags;
    private FrameLayout mFrameLayout;
    private Location mLastLocation;
    private DTSBus mNearestBus;
    private CardView mSuggestionContainer;
    private ObjectAnimator mSuggestionAnimator;

    public static Intent getDialogIntent(Context context, EatItem eatItem, @Nullable Location currentLocation)
    {
        return new Intent(context, EatDialogActivity.class).putExtra(getEatItemIntentKey(), eatItem).putExtra(getLocationIntentKey(), currentLocation);
    }

    public static String getEatItemIntentKey()
    {
        return EatDialogActivity.class.getSimpleName() + "$eat_key";
    }

    public static String getLocationIntentKey()
    {
        return EatDialogActivity.class.getSimpleName() + "$loc_key";
    }

    @Override
    protected void onDestroy()
    {
        unbind();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.onCreate(savedInstanceState);
        mEatItem = getIntent().getParcelableExtra(getEatItemIntentKey());
        mLastLocation = getIntent().getParcelableExtra(getLocationIntentKey());
        setContentView(R.layout.activity_dialog_eat);
        setup();
        bindData(mEatItem);
        weatherData = new Weather();
        weatherData.getWeather(this);


        float shortestDistance = -2;

        if (mLastLocation != null)
        {
            for (int i = 0; i < DTSBus.Dataset.getLength(); i++)
            {
                final DTSBus dtsBus = DTSBus.Dataset.getData(i);
                if (shortestDistance == -2)
                {
                    mNearestBus = dtsBus;

                    shortestDistance = mNearestBus.getDistanceTo(mLastLocation);
                }
                else
                {
                    final float distance = mNearestBus.getDistanceTo(dtsBus.getLocation());
                    if (distance <= shortestDistance)
                    {
                        mNearestBus = dtsBus;
                    }
                }
            }
            if (DTSBus.Dataset.isUnindentifiedStation(mNearestBus.getStation()))
            {
                mNearestBus = DTSBus.Dataset.getReplacementForUnindentifiedStation();
            }
        }
    }

    public void setup()
    {

        mImageView = (ImageView) findViewById(R.id.li_eh_image);
        mHeader = (TextView) findViewById(R.id.li_eh_header);
        mWifistatus = (TextView) findViewById(R.id.li_eh_wifistatus);
        mPromo = (TextView) findViewById(R.id.li_eh_promo);
        mCongestion = (TextView) findViewById(R.id.li_eh_congestion);
        walk = (ImageButton) findViewById(R.id.li_ehe_walk);
        car = (ImageButton) findViewById(R.id.li_ehe_car);
        bus = (ImageButton) findViewById(R.id.li_ehe_bus);
        taxi = (ImageButton) findViewById(R.id.li_ehe_taxi);
        mSuggestion = (TextView) findViewById(R.id.special_suggestion);
        mFrameLayout = (FrameLayout) findViewById(R.id.special_mapfragment);
        mSuggestionContainer = (CardView) findViewById(R.id.li_eh_detailview);


        mSuggestionAnimator = ObjectAnimator.ofFloat(mSuggestionContainer, "rotationX", 360).setDuration(250);
        mSuggestionAnimator.setInterpolator(new DecelerateInterpolator());
        mSuggestionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(final Animator animation)
            {
                ObjectAnimator.ofFloat(mSuggestion, "alpha", 0f).setDuration(100).start();
            }

            @Override
            public void onAnimationEnd(final Animator animation)
            {
                ObjectAnimator.ofFloat(mSuggestion, "alpha", 1f).setDuration(100).start();
            }

            @Override
            public void onAnimationCancel(final Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(final Animator animation)
            {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            mSuggestionAnimator.setAutoCancel(true);
        }
    }

    public void bindData(final EatItem eatItem)
    {

        Glide.with(this).load(eatItem.getImageUri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
        mHeader.setText(eatItem.getHeader());
        mWifistatus.setText(eatItem.getWifiStatus());
        if (eatItem.isWifiAvailable())
        {
            mWifistatus.setTextColor(getResources().getColor(R.color.textColorPositive));
        }
        else
        {
            mWifistatus.setTextColor(getResources().getColor(R.color.textColorNegative));
        }

        final String promo = eatItem.getPromo();
        if (promo != null)
        {
            mPromo.setText(promo);
        }
        else
            mPromo.setVisibility(View.GONE);
        mCongestion.setText(eatItem.getCongestion());

        final GoogleMapOptions googleMapOptions = new GoogleMapOptions();

        if (mEatItem.getLatLng() != null)
        {
            googleMapOptions.camera(new CameraPosition(mEatItem.getLatLng(), 17f, 65f, 0f));
        }

        mMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        getSupportFragmentManager().beginTransaction().add(mFrameLayout.getId(), mMapFragment, "maps").commit();
        mMapFragment.getMapAsync(EatDialogActivity.this);

        for (Drawable drawable : Arrays.asList(mSuggestion.getCompoundDrawables()))
        {
            if (drawable != null)
            {
                DrawableCompat.setTint(drawable, getResources().getColor(R.color.textColorPositive));
            }
        }

    }

    private void unbind()
    {
        //getSupportFragmentManager().beginTransaction().remove(mMapFragment).commit();
    }

    public void switchSuggestion(View view)
    {
        final int id = view.getId();

        mSuggestionAnimator.start();

        if (id == walk.getId())
        {
            int choice = new Random(System.currentTimeMillis()).nextInt(2);
//            if (weatherData.isProcessing() || weatherData.isSunnyCurrently())
//            {
//                String[] choiceArr = new String[]{"Might be a good day for quick stroll!", "The coast is clear!", "Somewhat cloudy with a chance of yummy meatballs"};
//                mSuggestion.setText(choiceArr[choice]);
//            }

            String[] choiceArr = new String[]{"Might be a good day for quick stroll!", "The coast is clear!", "Somewhat cloudy with a chance of yummy meatballs"};
            mSuggestion.setText(choiceArr[choice]);
            // mSuggestion.setCompoundDrawables(walk.getDrawable(), null, null, null);
        }
        else if (id == car.getId())
        {
            mSuggestion.setText("Nearest parking spot with available parking:\nPerhentian Bas Cyberjaya (Terminal)");
            //mSuggestion.setCompoundDrawables(car.getDrawable(), null, null, null);
        }
        else if (id == bus.getId())
        {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String station;
            if (mLastLocation != null)
                station = mNearestBus.getStation().trim();
            else
                station = DTSBus.Dataset.getReplacementForUnindentifiedStation().getStation().trim();

            calendar.roll(Calendar.HOUR_OF_DAY, 1);
            if (calendar.get(Calendar.HOUR_OF_DAY) > 12)
            {
                mSuggestion.setText("Going back? Next bus near you at:\n" + station + " will be arriving at " + dateFormat.format(calendar.getTime()));

            }
            else
            {
                mSuggestion.setText("Going out? Next DTS bus at:\n" + station + " will be arriving at " + dateFormat.format(calendar.getTime()));

            }
            //mSuggestion.setCompoundDrawables(drawable, null, null, null);

        }
        else if (id == taxi.getId())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setPositiveButton("Launch", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    final String appPackageName = "com.myteksi.passenger";
                    Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
                    if (intent == null)
                    {
                        try
                        {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                        catch (android.content.ActivityNotFoundException anfe)
                        {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                    startActivity(intent);
                }
            }).setCancelable(true).setNeutralButton("Dismiss", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    dialog.dismiss();
                }
            }).setTitle("Launch MyTeksi app?").setMessage("We will direct you to its Play Store listing instead if you haven't installed it");

            builder.show();

            //mSuggestion.setCompoundDrawables(taxi.getDrawable(), null, null, null);

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        if (mEatItem != null)
        {
            LatLng latLng = mEatItem.getLatLng();
            if (latLng != null)
            {
                final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(mEatItem.getHeader());
                if (mLastLocation != null)
                {
                    markerOptions.snippet(Util.formatDistanceInMeters(mLastLocation.distanceTo(mEatItem.getLocation()), 1));
                }
                googleMap.addMarker(markerOptions).showInfoWindow();
                if (mLastLocation != null)
                {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(mNearestBus.getLatitude(), mNearestBus.getLongitude())).title(mNearestBus.getStation()).snippet(mNearestBus.getLocationHint().trim()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
        }
    }

    public void dismiss(View view)
    {
        setResult(0);
        finish();
    }


    @Override
    public void onWeatherReady(final Weather weather)
    {

    }

    public void launchMaps(View view)
    {
        Uri data = Uri.parse("geo:0,0?q=" + mEatItem.getLatLng().latitude + "," + mEatItem.getLatLng().longitude + "(" + mEatItem.getHeader() + ")");


        Intent intent = new Intent(Intent.ACTION_VIEW).setData(data);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
        setResult(1);
        finish();
    }
//
//    View view = LayoutInflater.from(this).inflate(R.layout.listitem_eathome_expanded, parent, false);
//    final EatItem.ExpandedView expandedView = new EatItem.ExpandedView(view);
//    expandedView.bindData(eatItem, this);
//
//    AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view).setPositiveButton("I'M COMING", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(final DialogInterface dialog, final int which)
//        {
//            expandedView.onUnbind(EatHomeActivity.this);
//        }
//    }).setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(final DialogInterface dialog, final int which)
//        {
//            expandedView.onUnbind(EatHomeActivity.this);
//        }
//    });
//
//    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//    @Override
//    public void onCancel(final DialogInterface dialog)
//    {
//        expandedView.onUnbind(EatHomeActivity.this);
//    }
//});
//    builder.show();
}

