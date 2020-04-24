package com.inspierra.fishapp.Activities.ui.farmerconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.PickType;
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.Activities.SearchFarmsResultActivity;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

//import com.inspierra.fishapp.Activities.R;

public class FarmerConnectFragment_bckOld extends Fragment {

    private FarmerConnectViewModel notificationsViewModel;

    CustomEditText txtKeyWordSearch;
    RadioGroup rdgSearchMode;
    Button btnSelectDateRange;
    LoadingButton btnSubmit;
    ProgressDialog progressDialog;
    AcquahService acquahService;
     String _activitySource;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(FarmerConnectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_form, container, false);
       // final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        _activitySource = getActivity().getIntent().getStringExtra("TRACKER_VIEW");
        Log.i("Search","Activity source>>"+_activitySource);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        txtKeyWordSearch = root.findViewById(R.id.txtKeyWordSearch);
        rdgSearchMode = root.findViewById(R.id.rdgSearchMode);
        btnSelectDateRange= root.findViewById(R.id.btnSelectDateRange);
        btnSubmit= root.findViewById(R.id.btnSubmit);
        CivilCalendar calendar = new CivilCalendar(Locale.ENGLISH);
        CivilCalendar calendar1 =new CivilCalendar(Locale.ENGLISH);
        calendar1.setYear(2015);
        calendar1.setDayOfMonth(1);
        calendar1.setMonth(1);
        PrimeDatePickerBottomSheet datePicker = PrimeDatePickerBottomSheet.newInstance(calendar,calendar1, PickType.RANGE_START);

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
            datePicker.show(getActivity().getSupportFragmentManager(), "SOME_TAG");
        });

        btnSubmit.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchFarmsResultActivity.class));
        });

        return root;
    }




}
