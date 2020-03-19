package com.inspierra.fishapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataResponseClass;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProductionDataActivity extends AppCompatActivity
{
    RelativeLayout relPondCages;
    RelativeLayout relHatcheries;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    SaveProductionDataRequestClass requestClass;
    CustomEditText PondID, txtQuantityOfFish, txtDateOfStocking,txtFishSize,txtSourceOfFeed,txtHarvestDate,
    txtQuantityHarvested,txtWeightOfFishHarvested,txtPriceOfFishPerKg,
    txtIncubatedEggs,txtEggsHatched,txtIncubationDuration,txtNoOfFriesStocked,txtNoOfFriesRecovered,txtSexReversalDuration,txtAvgWeightOfStocking,
            txtWeightReversal,txtNoOfFriesNursery,txtStockingRecovered,txtNurseryDuration,txtFinalAvgWeight;



    LoadingButton btnSubmit;
    UserPondsClass pond;
    ImageView FishDisease;
    boolean pondMode;
    RelativeLayout relRecord,relback;
    String filePath;
    Spinner qtyFeed;
    int color;
    int requestCode = 0;
    String[] feedlist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_data_layout);

        Intent intent = getIntent();
        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        color = getResources().getColor(R.color.blueGray200);

        pond = new Gson().fromJson(intent.getStringExtra("pond"), UserPondsClass.class);
        ActivityCompat.requestPermissions(this,
                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);
        //ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);

        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
          /* setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);*/

        }

        try
        {
            if (pond.pondId > 0)
            {
                requestClass = new SaveProductionDataRequestClass();
                requestClass.pondId = pond.pondId;
                //progressDialog.show();
                //new getProductionData().execute();
            }
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }

        relPondCages = findViewById(R.id.relPondCages);
        relHatcheries = findViewById(R.id.relHatcheries);
        qtyFeed = findViewById(R.id.spnFeed);
        relback = findViewById(R.id.relback);
        relback.setOnClickListener(v -> onBackPressed());
        relRecord = findViewById(R.id.relRecord);
        PondID = findViewById(R.id.PondID);

        txtQuantityOfFish = findViewById(R.id.txtQuantityOfFish);
        txtDateOfStocking= findViewById(R.id.txtDateOfStocking);
        txtFishSize= findViewById(R.id.txtFishSize);
        txtSourceOfFeed= findViewById(R.id.txtSourceOfFeed);
         txtHarvestDate= findViewById(R.id.txtHarvestDate);
        txtQuantityHarvested= findViewById(R.id.txtQuantityHarvested);
        txtWeightOfFishHarvested= findViewById(R.id.txtWeightOfFishHarvested);
        txtPriceOfFishPerKg= findViewById(R.id.txtPriceOfFishPerKg);

        txtIncubatedEggs = findViewById(R.id.txtIncubatedEggs);
        txtEggsHatched = findViewById(R.id.txtEggsHatched);
        txtIncubationDuration = findViewById(R.id.txtIncubationDuration);
        txtNoOfFriesStocked = findViewById(R.id.txtNoOfFriesStocked);
        txtNoOfFriesRecovered = findViewById(R.id.txtNoOfFriesRecovered);
        txtSexReversalDuration = findViewById(R.id.txtSexReversalDuration);
        txtAvgWeightOfStocking = findViewById(R.id.txtAvgWeightOfStocking);
        txtWeightReversal = findViewById(R.id.txtWeightReversal);
        txtNoOfFriesNursery = findViewById(R.id.txtNoOfFriesNursery);
        txtStockingRecovered = findViewById(R.id.txtStockingRecovered);
        txtNurseryDuration = findViewById(R.id.txtNurseryDuration);
        txtFinalAvgWeight = findViewById(R.id.txtFinalAvgWeight);
        txtIncubationDuration = findViewById(R.id.txtIncubationDuration);
        btnSubmit = findViewById(R.id.btnSubmit);

        qtyFeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                feedlist = getResources().getStringArray(R.array.feed);
               // requestClass.quantityOfFeedPerDay = Integer.parseInt(feedlist[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        if(pond.pondType.equals("Pond") || pond.pondType.equals("Cage")){
            relHatcheries.setVisibility(View.GONE);
            relPondCages.setVisibility(View.VISIBLE);
            pondMode=true;
        }else{
            relHatcheries.setVisibility(View.VISIBLE);
            relPondCages.setVisibility(View.GONE);
        }
        btnSubmit.setOnClickListener(v -> {
            progressDialog.show();
            requestClass.pondId = Integer.parseInt(PondID.getText().toString().trim());
            if(pondMode){
                requestClass.quantityOfFish = txtQuantityOfFish.getText().toString().trim();
                requestClass.dateOfStocking = txtDateOfStocking.getText().toString().trim();
                requestClass.sizeOfFish = Integer.parseInt(txtFishSize.getText().toString().trim());
                requestClass.quantityOfFeedPerDay = Float.parseFloat(qtyFeed.getSelectedItem().toString().trim());
                requestClass.sourceOfFeed = txtSourceOfFeed.getText().toString().trim();
                requestClass.harvestDate = txtHarvestDate.getText().toString().trim();
                requestClass.quantityOfFishHarvested = Integer.parseInt(txtQuantityHarvested.getText().toString().trim());
                requestClass.weightOfFishHarvested = Integer.parseInt(txtWeightOfFishHarvested.getText().toString().trim());
                requestClass.pricePerKg = Integer.parseInt(txtPriceOfFishPerKg.getText().toString().trim());
            }else{
                requestClass.incubatedEggs = Integer.parseInt(txtIncubatedEggs.getText().toString().trim());
                requestClass.hatchedEggs = Integer.parseInt(txtEggsHatched.getText().toString().trim());
                requestClass.incubationDuration = Integer.parseInt(txtIncubationDuration.getText().toString().trim());
                requestClass.noOfFriesStocked = Integer.parseInt(txtNoOfFriesStocked.getText().toString().trim());
                requestClass.noOfFriesRecovered = Integer.parseInt(txtNoOfFriesRecovered.getText().toString().trim());
                requestClass.sexReversalDuration = Integer.parseInt(txtSexReversalDuration.getText().toString().trim());
                requestClass.averageWeightStocking = Integer.parseInt(txtAvgWeightOfStocking.getText().toString().trim());
                requestClass.averageWeightReversal = Integer.parseInt(txtWeightReversal.getText().toString().trim());
                requestClass.noOfFriesStockingNursery = Integer.parseInt(txtNoOfFriesNursery.getText().toString().trim());
                requestClass.noOfFriesStockingRecovered = Integer.parseInt(txtNoOfFriesRecovered.getText().toString().trim());
                requestClass.durationOfNursery = Integer.parseInt(txtNurseryDuration.getText().toString().trim());
                requestClass.finalAverageWeight = Integer.parseInt(txtFinalAvgWeight.getText().toString().trim());
            }
            new addProductionData().execute();
        });

        relRecord.setOnClickListener(v -> AndroidAudioRecorder.with(ProductionDataActivity.this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)
                // Start recording
                .record());
    }

    class addProductionData extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SaveProductionDataResponseClass> addData = acquahService.SaveProductionData(requestClass,
                    PrefsUtil.getTempTokenData(ProductionDataActivity.this));
            addData.enqueue(new Callback<SaveProductionDataResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SaveProductionDataResponseClass> call,
                                       @NotNull Response<SaveProductionDataResponseClass> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if (response.body().status.equals("00"))
                        {
                            Toast.makeText(ProductionDataActivity.this, " Data Added Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProductionDataActivity.this, " Data Add Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(ProductionDataActivity.this, " Add Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SaveProductionDataResponseClass> call, @NotNull Throwable t)
                {
                    Toast.makeText(ProductionDataActivity.this, " Data Add Failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

    class getProductionData extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SaveProductionDataRequestClass> getFishHealth = acquahService.GetProductionData(requestClass,
                    PrefsUtil.getTempTokenData(ProductionDataActivity.this));
            getFishHealth.enqueue(new Callback<SaveProductionDataRequestClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SaveProductionDataRequestClass> call,
                                       @NotNull Response<SaveProductionDataRequestClass> response)
                {
                    try
                    {
                        assert response.body() != null;
                        if (response.body().pondId > 0)
                        {
                            txtQuantityOfFish.setText(response.body().quantityOfFish);
                           /* dateofStocking = findViewById(R.id.dateofStocking);
                            fishSize = findViewById(R.id.fishSize);
                            Nitrite = findViewById(R.id.Nitrite);
                            Turbidity = findViewById(R.id.Turbidity);
                            eggsHatched = findViewById(R.id.eggsHatched);
                            incubateTime = findViewById(R.id.incubateTime);
                            noOfFries = findViewById(R.id.noOfFries);
                            noOfFriesRecovered = findViewById(R.id.noOfFriesRecovered);
                            SexReversal = findViewById(R.id.SexReversal);
                            stockingWeight = findViewById(R.id.stockingWeight);
                            weightReversal = findViewById(R.id.weightReversal);
                            noOfFriesNursery = findViewById(R.id.noOfFriesNursery);
                            friesRecovered = findViewById(R.id.friesRecovered);
                            nurseryDuration = findViewById(R.id.nurseryDuration);
                            avgWeight = findViewById(R.id.avgWeight);
                            sourceOfFeed = findViewById(R.id.sourceOfFeed);
                            fishDisease = findViewById(R.id.fishDisease);*/

                        }
                    }
                    catch (Exception ex)
                    {
                        String error = ex.getMessage();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SaveProductionDataRequestClass> call, @NotNull Throwable t)
                {

                }
            });
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);*/
        }
        else
        {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Glide.with(this).load(photo).into(FishDisease);
        }

        //record response
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                // Great! User has recorded and saved the audio file
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // Oops! User has canceled the recording
            }
        }
    }

}
