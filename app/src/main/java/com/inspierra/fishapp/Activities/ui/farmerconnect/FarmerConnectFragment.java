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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

//import com.inspierra.fishapp.Activities.R;
import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.PickType;
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.Activities.HomePageActivity;
import com.inspierra.fishapp.Activities.SearchFarmsResultActivity;
import com.inspierra.fishapp.Adapter.PondsAdapter;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class FarmerConnectFragment extends Fragment {

    private FarmerConnectViewModel notificationsViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(FarmerConnectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_farmer_connect, container, false);
       // final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        setActionBarTitle("Farmer Connect");
        return root;
    }

    private void setActionBarTitle(String title){
        ((HomePageActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(title);
    }



}
