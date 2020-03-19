package com.inspierra.fishapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.R;

public class MainActivity extends AppCompatActivity
{
    LoadingButton btnRegister, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT < 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //*  setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);*//*
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            //this.finish();
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            //this.finish();
        });

        //startActivity(new Intent(this,Hub.class));
    }
}
