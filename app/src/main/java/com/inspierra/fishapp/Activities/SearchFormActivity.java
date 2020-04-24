package com.inspierra.fishapp.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primecalendar.persian.PersianCalendar;
import com.aminography.primedatepicker.PickType;
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.Adapter.PondsAdapter;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.BusStation;
import com.libizo.CustomEditText;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Locale;


public class SearchFormActivity extends AppCompatActivity
{
    CustomEditText txtKeyWordSearch;
    RadioGroup rdgSearchMode;
    Button btnSelectDateRange;
    LoadingButton btnSubmit;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    ArrayList<UserPondsClass> ponss;
    PondsAdapter pondsAdapter;
    RelativeLayout relback;
    String _activitySource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_form);
        BusStation.getBus().register(this);
        if (Build.VERSION.SDK_INT < 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if(Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        _activitySource = getIntent().getStringExtra("TRACKER_VIEW");
        Log.i("Search","Activity source>>"+_activitySource);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        relback = findViewById(R.id.relback);
        relback.setOnClickListener(v -> onBackPressed());
        txtKeyWordSearch = findViewById(R.id.txtKeyWordSearch);
        rdgSearchMode = findViewById(R.id.rdgSearchMode);
        btnSelectDateRange= findViewById(R.id.btnSelectDateRange);
        btnSubmit= findViewById(R.id.btnSubmit);
        CivilCalendar calendar = new CivilCalendar(Locale.ENGLISH);
        CivilCalendar calendar1 =new CivilCalendar(Locale.ENGLISH);
        calendar1.setYear(2015);
        calendar1.setDayOfMonth(1);
        calendar1.setMonth(1);
        PrimeDatePickerBottomSheet datePicker = PrimeDatePickerBottomSheet.newInstance(calendar,calendar1,PickType.RANGE_START);

        datePicker.setOnDateSetListener(new PrimeDatePickerBottomSheet.OnDayPickedListener() {
            @Override
            public void onSingleDayPicked(@NotNull PrimeCalendar singleDay) {
                // TODO
            }
            @Override
            public void onRangeDaysPicked(@NotNull PrimeCalendar startDay, @NotNull PrimeCalendar endDay) {
                // TODO
            }
        });

        btnSelectDateRange.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), "SOME_TAG");
        });

        btnSubmit.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchFarmsResultActivity.class));
        });

    }




}
