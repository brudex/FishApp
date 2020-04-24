package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.AddPondRequestClass;
import com.inspierra.fishapp.HelpingClasses.AddPondResponseClass;

import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.BusStation;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;
import com.raywenderlich.android.validatetor.ValidateTor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPondsActivity extends AppCompatActivity
{
    CustomEditText pondName, cubicSize;
    MaterialCheckBox chk_large, chk_small;
    LoadingButton btnSubmit;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    AddPondRequestClass requestClass;
    Spinner pondTypes;
    ValidateTor validateTor = new ValidateTor();
    List<String> validationErrors;
    List<String> productionUnitTypes;
    UserPondsClass pond;
    RelativeLayout relback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ponds_layout);
        BusStation.getBus().register(this);
        validationErrors=new ArrayList<>();
        Intent intent = getIntent();
        pond = new Gson().fromJson(intent.getStringExtra("pond"), UserPondsClass.class);

        if (Build.VERSION.SDK_INT < 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        requestClass = new AddPondRequestClass();
        relback = findViewById(R.id.imgBack);
        relback.setOnClickListener(v -> onBackPressed());
        pondName = findViewById(R.id.pondName);
        cubicSize = findViewById(R.id.cubicSize);
        chk_large = findViewById(R.id.chkBxBig);
        chk_small = findViewById(R.id.chkBxSmall);
        btnSubmit = findViewById(R.id.btnSubmit);
        pondTypes = findViewById(R.id.pondTypes);

        try
        {
            if (pond.pondId > 0)
            {
                requestClass.pondId = pond.pondId;
                pondName.setText(pond.pondName);
                cubicSize.setText(pond.cubicSize);
                if (pond.isLargest)
                {
                    chk_large.setChecked(true);
                    chk_small.setChecked(false);
                }
                if (pond.isSmallest)
                {
                    chk_large.setChecked(false);
                    chk_small.setChecked(true);
                }
            }
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }
        new getPondTypes().execute();
        pondTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(productionUnitTypes!=null){
                    requestClass.pondType=productionUnitTypes.get(position);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        chk_large.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                chk_small.setChecked(false);
                requestClass.isLargest = true;
                requestClass.isSmallest = false;
            }
        });

        chk_small.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked)
            {
                chk_large.setChecked(false);
                requestClass.isLargest = false;
                requestClass.isSmallest = true;
            }
        });

        btnSubmit.setOnClickListener(v -> {
            requestClass.pondName = Objects.requireNonNull(pondName.getText()).toString().trim();
            if(validateFields()){
                requestClass.cubicSize = Integer.parseInt(Objects.requireNonNull(cubicSize.getText()).toString().trim());
                progressDialog.show();
                new doAddPond().execute();
            }else{
                Toast.makeText(this,"Please complete all field correctly",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean validateFields(){
        validationErrors.clear();
        if (validateTor.isEmpty(cubicSize.getText().toString()) ) {
            cubicSize.setError("Cubic size is required");
            validationErrors.add("Cubic size is required");
        }
        if (!validateTor.isNumeric(cubicSize.getText().toString()) ) {
            cubicSize.setError("Cubic size must be numeric");
            validationErrors.add("Cubic size must be numeric");
        }
        if (validateTor.isEmpty(requestClass.pondName)) {
            pondName.setError("Production unit name is required");
            validationErrors.add("Production unit name is required");
        }
        if (validateTor.isEmpty(requestClass.pondType)) {
            Toast.makeText(this,"Select production unit type",Toast.LENGTH_LONG).show();
            validationErrors.add("Production unit type is required");
        }
        return validationErrors.size() <= 0;
    }


    class doAddPond extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                final Call<AddPondResponseClass> addPond = acquahService.AddPond(requestClass,
                        PrefsUtil.getTempTokenData(MyPondsActivity.this));
                addPond.enqueue(new Callback<AddPondResponseClass>()
                {
                    @Override
                    public void onResponse(@NotNull Call<AddPondResponseClass> call,
                                           @NotNull Response<AddPondResponseClass> response)
                    {
                        progressDialog.dismiss();
                        try
                        {
                            assert response.body() != null;
                            if (response.body().status.equals("00"))
                            {
                                UserPondsClass bb = new UserPondsClass();
                                bb.identity=4;
                                BusStation.getBus().post(bb);
                                new OoOAlertDialog.Builder(MyPondsActivity.this)
                                        .setTitle("Success")
                                        .setMessage("Production unit Added Successfully")
                                        .setAnimation(Animation.POP)
                                        .setPositiveButton("Ok", new OnClickListener() {
                                            @Override
                                            public void onClick() {
                                                onBackPressed();
                                            }
                                        })
                                        .setPositiveButtonColor(R.color.green)
                                        .build();
                            }
                            else
                            {
                                Toast.makeText(MyPondsActivity.this, "Adding Production unit failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(MyPondsActivity.this, "Adding Production unit Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AddPondResponseClass> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MyPondsActivity.this, "Pond Data Add Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();
            }
            return null;
        }
    }

    class getPondTypes extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<List<String>> getPonds =
                    acquahService.GetPondTypes(PrefsUtil.getTempTokenData(MyPondsActivity.this));
            getPonds.enqueue(new Callback<List<String>>()
            {
                @Override
                public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response)
                {
                    assert response.body() != null;
                    productionUnitTypes = response.body();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MyPondsActivity.this,
                            android.R.layout.simple_spinner_item, productionUnitTypes);
                    runOnUiThread(() -> pondTypes.setAdapter(dataAdapter));
                }

                @Override
                public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t)
                {

                }
            });
            return null;
        }
    }
}
