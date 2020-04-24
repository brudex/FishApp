package com.inspierra.fishapp.Activities.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alespero.expandablecardview.ExpandableCardView;
import com.inspierra.fishapp.Activities.EconIndicator;
import com.inspierra.fishapp.Activities.EconomicIndicatorOutput;
import com.inspierra.fishapp.Activities.FishHealthActivity;
import com.inspierra.fishapp.Activities.HomePageActivity;
import com.inspierra.fishapp.Activities.PondsListActivity;
import com.inspierra.fishapp.Activities.ProductionDataActivity;
import com.inspierra.fishapp.Activities.ui.HomeViewModel;
import com.inspierra.fishapp.R;

import java.util.Objects;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class DashboardFragment extends Fragment {

    RelativeLayout econ_indicator, fish_health, pdata,productionUnitDash,economicIndicatorDash,relPermits;
    androidx.cardview.widget.CardView cardProductionData;
    androidx.cardview.widget.CardView cardEconomicIndicators;
    androidx.cardview.widget.CardView cardEconomicIndicatorsOut,cardProductionDataOut,cardPermits;
    com.alespero.expandablecardview.ExpandableCardView expandableInputs,expandableOutputs;

    private HomeViewModel  viewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.updateActionBarTitle("Custom Title From Fragment");
        expandableInputs  = root.findViewById(R.id.expandableInputs);
        expandableOutputs= root.findViewById(R.id.expandableOutputs);

        cardProductionData = root.findViewById(R.id.cardProductionData);
        cardEconomicIndicators = root.findViewById(R.id.cardEconomicIndicators);
        cardEconomicIndicatorsOut = root.findViewById(R.id.cardEconomicIndicatorsOut);
        cardProductionDataOut = root.findViewById(R.id.cardProductionDataOut);
        cardPermits = root.findViewById(R.id.cardPermits);

        relPermits = root.findViewById(R.id.relPermits);
        productionUnitDash = root.findViewById(R.id.relDashProductionUnit);
        economicIndicatorDash = root.findViewById(R.id.relDashEconIndicator);
        econ_indicator = root.findViewById(R.id.rel2);
        fish_health = root.findViewById(R.id.rel3);
        pdata = root.findViewById(R.id.rel4);

        cardEconomicIndicators.setOnClickListener(v -> startActivity(new Intent(getActivity(), EconIndicator.class)));
        cardEconomicIndicatorsOut.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EconomicIndicatorOutput.class));
        });

        cardProductionData.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PondsListActivity.class);
            intent.putExtra("TRACKER_VIEW", "DATA_INPUT");
            startActivity(intent);
        });
        cardProductionDataOut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PondsListActivity.class);
            intent.putExtra("TRACKER_VIEW", "OUTPUTS");
            startActivity(intent);
        });

        cardPermits.setOnClickListener(v -> {
            new OoOAlertDialog.Builder(getActivity())
                    .setTitle("Coming Soon")
                    .setMessage("Service not available at the moment")
                    .setAnimation(Animation.POP)
                    .setPositiveButton("Ok", null)
                    .build();
        });

        expandableInputs.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(expandableOutputs.isExpanded())
                expandableOutputs.collapse();
            }
        });
        expandableOutputs.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(expandableInputs.isExpanded())
                  expandableInputs.collapse();
            }
        });

        setActionBarTitle("Dashboard");
        return root;
    }

    private void setActionBarTitle(String title){
        ((HomePageActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(title);
    }



}
