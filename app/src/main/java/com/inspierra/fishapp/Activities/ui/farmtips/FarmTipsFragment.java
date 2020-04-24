package com.inspierra.fishapp.Activities.ui.farmtips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.inspierra.fishapp.Activities.HomePageActivity;
import com.inspierra.fishapp.Activities.MyPondsActivity;
import com.inspierra.fishapp.Activities.PondsListActivity;
import com.inspierra.fishapp.Adapter.FarmTipsListAdapter;
import com.inspierra.fishapp.Adapter.PondsAdapter;
import com.inspierra.fishapp.HelpingClasses.FarmTipsClass;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.ItemClickSupport;
import com.inspierra.fishapp.Utilities.PrefsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.inspierra.fishapp.Activities.R;

public class FarmTipsFragment extends Fragment {

    RecyclerView farmTipsView;
     ProgressDialog progressDialog;
    AcquahService acquahService;
    ArrayList<FarmTipsClass> farmTipsList;
    FarmTipsListAdapter farmTipsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_farm_tips, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);

        farmTipsView = root.findViewById(R.id.listFarmTips);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        farmTipsView.setHasFixedSize(true);
        farmTipsView.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(farmTipsView).setOnItemClickListener((recyclerView, position, v) -> {
            Intent mIntent = new Intent(getActivity(), MyPondsActivity.class);
            mIntent.putExtra("pond", new Gson().toJson(farmTipsList.get(position)));
            startActivity(mIntent);
        });


        new GetFarmTips().execute();
       progressDialog.show();
        setActionBarTitle("Farm Tips");
        return root;
    }

    private void setActionBarTitle(String title){
        ((HomePageActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(title);
    }
    class GetFarmTips extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<ArrayList<FarmTipsClass>> getponds =
                    acquahService.GetFarmTips(PrefsUtil.getTempTokenData(getActivity()));
            getponds.enqueue(new Callback<ArrayList<FarmTipsClass>>()
            {
                @Override
                public void onResponse(@NotNull Call<ArrayList<FarmTipsClass>> call,
                                       @NotNull Response<ArrayList<FarmTipsClass>> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if (response.body().size() > 0)
                        {
                            farmTipsList = new ArrayList<>();
                            farmTipsList.addAll(response.body());
                            farmTipsAdapter = new FarmTipsListAdapter(getActivity(), response.body());
                            getActivity().runOnUiThread(() -> farmTipsView.setAdapter(farmTipsAdapter));
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getActivity(), "Error Loading Ponds", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<FarmTipsClass>> call, @NotNull Throwable t)
                {
                    Toast.makeText(getActivity(), "Error Farm Tips", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }



}
