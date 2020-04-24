package com.inspierra.fishapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.inspierra.fishapp.HelpingClasses.LoginResponseClass;
import com.inspierra.fishapp.HelpingClasses.SearchFarmersRequest;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.PrefsUtil;

public class Hub extends AppCompatActivity
{
    CardView myProfile,farmTracker,farmConnect,farmTips;
   // LoginResponseClass userData;
    TextView uname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub_view);

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
          /* setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);*/
        }

        myProfile = findViewById(R.id.myProfile);
        farmTracker = findViewById(R.id.farmTracker);
        farmConnect = findViewById(R.id.farmConnect);
        farmTips = findViewById(R.id.farmTips);
       // userData = PrefsUtil.getUserData(this);
        uname = findViewById(R.id.uname);

        myProfile.setOnClickListener(v -> {
            startActivity(new Intent(this,ProfileActivity.class));
        });
        farmTracker.setOnClickListener(v -> {
            startActivity(new Intent(this,FarmTrackerActivity.class));
        });
        farmConnect.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchFormActivity.class));
        });
        farmTips.setOnClickListener(v -> {
            startActivity(new Intent(this,FarmTipActivity.class));
        });
    }
}
