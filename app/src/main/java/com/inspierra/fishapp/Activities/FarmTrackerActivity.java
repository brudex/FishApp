package com.inspierra.fishapp.Activities;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.inspierra.fishapp.R;
import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class FarmTrackerActivity extends AppCompatActivity
{
    RelativeLayout ponds, econ_indicator, fish_health, pdata,productionUnitDash,economicIndicatorDash,relPermits;
    RelativeLayout relback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farm_tracker_layout);
        if(Build.VERSION.SDK_INT < 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if(Build.VERSION.SDK_INT >= 19)
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

        relPermits = findViewById(R.id.relPermits);
        relback = findViewById(R.id.relback);
        relback.setOnClickListener(v -> onBackPressed());
        ponds = findViewById(R.id.rel1);
        productionUnitDash = findViewById(R.id.relDashProductionUnit);
        economicIndicatorDash = findViewById(R.id.relDashEconIndicator);
        econ_indicator = findViewById(R.id.rel2);
        fish_health = findViewById(R.id.rel3);
        pdata = findViewById(R.id.rel4);

        ponds.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PondsListActivity.class);
                intent.putExtra("TRACKER_VIEW", "DATA_INPUT");
            startActivity(intent);
         });
        productionUnitDash.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PondsListActivity.class);
            intent.putExtra("TRACKER_VIEW", "OUTPUTS");
            startActivity(intent);
        });
        relPermits.setOnClickListener(v -> {
            new OoOAlertDialog.Builder(FarmTrackerActivity.this)
                    .setTitle("Coming Soon")
                    .setMessage("Service not available at the moment")
                     .setAnimation(Animation.POP)
                    .setPositiveButton("Ok", null)
                    .build();
        });
        econ_indicator.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EconIndicator.class)));
        fish_health.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FishHealthActivity.class)));
        pdata.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ProductionDataActivity.class)));
        economicIndicatorDash.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EconomicIndicatorOutput.class)));
    }
}
