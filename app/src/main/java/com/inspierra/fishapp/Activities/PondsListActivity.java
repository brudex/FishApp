package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.Adapter.PondsAdapter;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.BusStation;
import com.inspierra.fishapp.Utilities.ItemClickSupport;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PondsListActivity extends AppCompatActivity
{
    RecyclerView ponds;
    FloatingActionButton add_fab;
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
        setContentView(R.layout.pondslist_layout);
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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //*  setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);*//*
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        _activitySource = getIntent().getStringExtra("TRACKER_VIEW");

        Log.i("Chart","Activity source>>"+_activitySource);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        relback = findViewById(R.id.relback);
        relback.setOnClickListener(v -> onBackPressed());
        add_fab = findViewById(R.id.add_fab);
        ponds = findViewById(R.id.lstPonds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ponds.setHasFixedSize(true);
        ponds.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(ponds).setOnItemClickListener((recyclerView, position, v) -> {
            Intent mIntent = new Intent(this, MyPondsActivity.class);
            mIntent.putExtra("pond", new Gson().toJson(ponss.get(position)));
            startActivity(mIntent);
        });

        add_fab.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPondsActivity.class));
        });

        progressDialog.show();
        new getUserPonds().execute();

    }

    class getUserPonds extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<ArrayList<UserPondsClass>> getponds =
                    acquahService.GetUserPonds(PrefsUtil.getTempTokenData(PondsListActivity.this));
            getponds.enqueue(new Callback<ArrayList<UserPondsClass>>()
            {
                @Override
                public void onResponse(@NotNull Call<ArrayList<UserPondsClass>> call,
                                       @NotNull Response<ArrayList<UserPondsClass>> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if (response.body().size() > 0)
                        {
                            ponss = new ArrayList<>();
                            ponss.addAll(response.body());
                            pondsAdapter = new PondsAdapter(PondsListActivity.this, response.body());
                            runOnUiThread(() -> ponds.setAdapter(pondsAdapter));
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(PondsListActivity.this, "Error Loading Ponds", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<UserPondsClass>> call, @NotNull Throwable t)
                {
                    Toast.makeText(PondsListActivity.this, "Error Loading Ponds", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

    @Subscribe
    public void PondListener(UserPondsClass pond)
    {
        switch (pond.identity)
        {
            case 1:
                if(_activitySource.equals("DATA_INPUT")){
                    Intent mIntent = new Intent(this, FishHealthActivity.class);
                    mIntent.putExtra("pond", new Gson().toJson(pond));
                    startActivity(mIntent);
                }else{
                    Log.i("Charts","Calling FishHealthOutputActivity>>>");
                    Intent mIntent = new Intent(this, FishHealthOutputActivity.class);
                    startActivity(mIntent);
                }

                break;
            case 2:
                if(_activitySource.equals("DATA_INPUT")){
                    Intent mIntentt = new Intent(this, ProductionDataActivity.class);
                    mIntentt.putExtra("pond", new Gson().toJson(pond));
                    startActivity(mIntentt);
                }else{
                    Intent mIntent = new Intent(this, FishHealthOutputActivity.class);
                    mIntent.putExtra("pond", new Gson().toJson(pond));
                    startActivity(mIntent);
                }

                break;
            case 4:
                new getUserPonds().execute();
                break;
        }
    }
}
