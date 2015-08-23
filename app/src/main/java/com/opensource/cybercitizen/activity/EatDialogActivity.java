package com.opensource.cybercitizen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opensource.cybercitizen.R;
import com.opensource.cybercitizen.activity.model.EatItem;
import com.opensource.cybercitizen.controller.Weather;

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

    public static String getEatItemIntentKey()
    {
        return EatDialogActivity.class.getSimpleName() + "$eat_key";
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
        setContentView(R.layout.activity_dialog_eat);
        setup();
        bindData(mEatItem);
        weatherData = new Weather();
        weatherData.getWeather(this);
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

        mPromo.setText(eatItem.getPromo());
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
        if (id == walk.getId())
        {
            int choice = new Random(System.currentTimeMillis()).nextInt(2);
            if (weatherData.isProcessing() || weatherData.isSunnyCurrently())
            {
                String[] choiceArr = new String[]{"Might be a good day for quick stroll!", "The coast is clear!", "Somewhat cloudy with a chance of yummy meatballs"};
                mSuggestion.setText(choiceArr[choice]);
            }
        }
        else if (id == car.getId())
        {
            mSuggestion.setText("Nearest parking spot with available parking:\nPerhentian Cyberjaya (Terminal)");
        }
        else if (id == bus.getId())
        {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            if (calendar.get(Calendar.HOUR_OF_DAY) > 12)
            {
                calendar.add(Calendar.HOUR_OF_DAY, 1);

                mSuggestion.setText("Going back? Next bus near you: HSBC " + dateFormat.format(calendar.getTime()));
            }
            else
            {
                mSuggestion.setText("Going to work? Next DTS bus near your location will be arriving at " + dateFormat.format(calendar.getTime()));
            }
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
                googleMap.addMarker(new MarkerOptions().position(latLng).snippet("Good weather for walking"));
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
        Uri data = Uri.parse("geo:" + mEatItem.getLatLng().latitude + "," + mEatItem.getLatLng().longitude);


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
