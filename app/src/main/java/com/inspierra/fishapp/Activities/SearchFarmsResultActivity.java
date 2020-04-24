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

import com.google.gson.Gson;
import com.inspierra.fishapp.Adapter.SearchFarmsResultAdapter;
import com.inspierra.fishapp.HelpingClasses.FarmerInfoItem;
import com.inspierra.fishapp.HelpingClasses.SearchFarmersRequest;
import com.inspierra.fishapp.HelpingClasses.SearchResponseClass;
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

public class SearchFarmsResultActivity extends AppCompatActivity
{
    RecyclerView farmsListView;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    ArrayList<FarmerInfoItem> farmsResult;
    SearchFarmsResultAdapter searchResultsAdapter;
    RelativeLayout relback;
    String _activitySource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);
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
        relback = findViewById(R.id.imgBack);
        relback.setOnClickListener(v -> onBackPressed());
        farmsListView = findViewById(R.id.lstPonds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        farmsListView.setHasFixedSize(true);
        farmsListView.setLayoutManager(linearLayoutManager);
        progressDialog.show();
        new getFarmSearchResults().execute();
    }

    class getFarmSearchResults extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            SearchFarmersRequest request = new SearchFarmersRequest();
            final Call<SearchResponseClass> getSearchResults =    acquahService.FarmSearch(request,PrefsUtil.getTempTokenData(SearchFarmsResultActivity.this));
            getSearchResults.enqueue(new Callback<SearchResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SearchResponseClass> call,
                                       @NotNull Response<SearchResponseClass> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if(response.body().success){
                            farmsResult = new ArrayList<>();
                            farmsResult.addAll(response.body().data);
                            searchResultsAdapter = new SearchFarmsResultAdapter(SearchFarmsResultActivity.this, farmsResult);
                            runOnUiThread(() -> farmsListView.setAdapter(searchResultsAdapter));
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(SearchFarmsResultActivity.this, "Error Loading Results", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SearchResponseClass> call, @NotNull Throwable t)
                {
                    Toast.makeText(SearchFarmsResultActivity.this, "Error Loading Results", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }



}
