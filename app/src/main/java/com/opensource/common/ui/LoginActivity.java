package com.opensource.common.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opensource.cybercitizen.HomeActivity;
import com.opensource.cybercitizen.R;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
    }

    private void setupViews()
    {
        //
        ImageView imageView = (ImageView) findViewById(R.id.al_bg_imageview);
        Glide.with(this).load(R.raw.login_background).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(Color.BLACK).into(imageView);
    }

    public void executeLogin(View view)
    {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }
}
