package com.inspierra.fishapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.raywenderlich.android.validatetor.ValidateTor;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionDataActivity extends AppCompatActivity
{
    RelativeLayout relPondCages;
    RelativeLayout relHatcheries;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    SaveProductionDataRequestClass requestClass;
    CustomEditText  txtQuantityOfFish, txtDateOfStocking,txtFishSize,txtSourceOfFeed,txtHarvestDate,
    txtQuantityHarvested,txtWeightOfFishHarvested,txtPriceOfFishPerKg,
    txtIncubatedEggs,txtEggsHatched,txtIncubationDuration,txtNoOfFriesStocked,txtNoOfFriesRecovered,txtSexReversalDuration,txtAvgWeightOfStocking,
            txtWeightReversal,txtNoOfFriesNursery,txtStockingRecovered,txtNurseryDuration,txtFinalAvgWeight;
    TextView tvTitle,tvPondName;
    LoadingButton btnSubmit;
    UserPondsClass pond;
    ImageView FishDisease;
    boolean pondMode;
    RelativeLayout relRecord,relBack;
    String filePath;
    Spinner qtyFeed;
    int color;
    int requestCode = 0;
     String[] feedlist;
    ValidateTor validateTor = new ValidateTor();
    List<String> validationErrors;
    static int CONTROL_SOURCE = 0;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_data_layout);
        validationErrors=new ArrayList<>();
        Intent intent = getIntent();
        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        color = getResources().getColor(R.color.blueGray200);
        String pondJson = intent.getStringExtra("pond");
        pond = new Gson().fromJson(pondJson, UserPondsClass.class);
        Log.d("ProductionData",pondJson);
//        ActivityCompat.requestPermissions(this,
//                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);
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
        }

        try
        {
            if (pond.pondId > 0)
            {
                requestClass = new SaveProductionDataRequestClass();
                requestClass.pondId = pond.pondId;

            }
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }

        relPondCages = findViewById(R.id.relPondCages);
        relHatcheries = findViewById(R.id.relHatcheries);
        qtyFeed = findViewById(R.id.spnFeed);
        relBack = findViewById(R.id.imgBack);

        relRecord = findViewById(R.id.relRecord);
        relBack.setOnClickListener(v -> onBackPressed());
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
        tvTitle = findViewById(R.id.tvTitle);
        tvPondName = findViewById(R.id.tvPondName);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvPondName.setText(pond.pondName.toUpperCase());
        qtyFeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                feedlist = getResources().getStringArray(R.array.feed);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

         DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(CONTROL_SOURCE == 1){
                    updateControl(txtHarvestDate);
                }else if(CONTROL_SOURCE==2){
                    updateControl(txtDateOfStocking);
                }
            }

        };
         txtHarvestDate.setFocusable(true);
         txtHarvestDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(view.hasFocus()){
                    DatePickerDialog dialog =   new DatePickerDialog(ProductionDataActivity.this, onDateSetListener, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.setTitle("Select Harvest Date");
                    dialog.show();
                    CONTROL_SOURCE =1;
                }

            }
        });
        txtDateOfStocking.setFocusable(true);
        txtDateOfStocking.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(view.hasFocus()){
                    DatePickerDialog dialog =   new DatePickerDialog(ProductionDataActivity.this, onDateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.setTitle("Select Date of Stocking");
                    dialog.show();
                CONTROL_SOURCE =2;
               }
            }
        });
        if(pond.pondType==null){
            pond.pondType = "Hatcheries";
        }
        if(pond.pondType.equals("Pond") || pond.pondType.equals("Cage")){
            relHatcheries.setVisibility(View.GONE);
            relPondCages.setVisibility(View.VISIBLE);
            pondMode=true;
        }else{
            pondMode=false;
            relHatcheries.setVisibility(View.VISIBLE);
            relPondCages.setVisibility(View.GONE);
        }
        btnSubmit.setOnClickListener(v -> {
            if(validateFields()){
                progressDialog.show();
                requestClass.pondId = pond.pondId;
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
                    requestClass.durationOfNursery = Math.round(Float.parseFloat(txtNurseryDuration.getText().toString().trim()));
                    requestClass.finalAverageWeight = Float.parseFloat(txtFinalAvgWeight.getText().toString().trim());
                }
                new addProductionData().execute();
            }

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

    private void updateControl(EditText editText) {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }
    private boolean validateFields(){
        validationErrors.clear();
        if(pondMode){
            if (validateTor.isEmpty( txtQuantityOfFish.getText().toString())) {
                txtQuantityOfFish.setError("Quantity of fish is required");
                validationErrors.add("Quantity of fish is required");
            }
            if (validateTor.isEmpty(txtDateOfStocking.getText().toString())) {
                txtDateOfStocking.setError("Date of Stocking is required");
                validationErrors.add("Date of Stocking is required");
            }
            if (validateTor.isEmpty(txtFishSize.getText().toString())) {
                txtFishSize.setError("Fish size is required");
                validationErrors.add("Fish size is required");
            }
            if (!validateTor.isNumeric(txtFishSize.getText().toString())) {
                txtFishSize.setError("Fish size must be numeric");
                validationErrors.add("Fish size must be numeric");
            }
            if (validateTor.isEmpty(qtyFeed.getSelectedItem().toString())) {
                 Toast.makeText(getApplicationContext(),"Please select quantity of feed",Toast.LENGTH_SHORT).show();
                validationErrors.add("Please select quantity of feed");
            }
            if (validateTor.isEmpty(txtSourceOfFeed.getText().toString())) {
                txtSourceOfFeed.setError("Source of feed is required");
                validationErrors.add("Source of feed is required");
            }
            if (validateTor.isEmpty(txtHarvestDate.getText().toString())) {
                txtHarvestDate.setError("Harvest date is required");
                validationErrors.add("Harvest date is required");
            }
            if (validateTor.isEmpty(txtQuantityHarvested.getText().toString())) {
                txtQuantityHarvested.setError("Quantity Harvested is required");
                validationErrors.add("Harvest date is required");
            }
            if (validateTor.isEmpty(txtWeightOfFishHarvested.getText().toString())) {
                txtWeightOfFishHarvested.setError("Weight of fish is required");
                validationErrors.add("Weight of fish is required");
            }
            if (validateTor.isEmpty(txtPriceOfFishPerKg.getText().toString())) {
                txtPriceOfFishPerKg.setError("Price of fish is required");
                validationErrors.add("Price of fish is required");
            }
        }else{
            if (validateTor.isEmpty(txtIncubatedEggs.getText().toString())) {
                txtIncubatedEggs.setError("Incubated eggs is required");
                validationErrors.add("Incubated eggs is required");
            }
            if (validateTor.isEmpty(txtEggsHatched.getText().toString())) {
                txtEggsHatched.setError("Eggs hatched is required");
                validationErrors.add("Eggs hatched is required");
            }
            if (validateTor.isEmpty(txtIncubationDuration.getText().toString())) {
                txtIncubationDuration.setError("Incubation duration is required");
                validationErrors.add("Incubation duration is required");
            }
            if (validateTor.isEmpty(txtNoOfFriesStocked.getText().toString())) {
                txtNoOfFriesStocked.setError("No of fries stocked is required");
                validationErrors.add("No of fries stocked is required");
            }
            if (validateTor.isEmpty(txtNoOfFriesRecovered.getText().toString())) {
                txtNoOfFriesRecovered.setError("No of fries recovered is required");
                validationErrors.add("No of fries recovered is required");
            }
            if (validateTor.isEmpty(txtSexReversalDuration.getText().toString())) {
                txtSexReversalDuration.setError("Required");
                validationErrors.add("Required");
            }
            if (validateTor.isEmpty(txtAvgWeightOfStocking.getText().toString())) {
                txtAvgWeightOfStocking.setError("Required");
                validationErrors.add("Required");
            }
            if (validateTor.isEmpty(txtWeightReversal.getText().toString())) {
                txtWeightReversal.setError("Required");
                validationErrors.add("Required");
            }
            if (validateTor.isEmpty(txtNoOfFriesNursery.getText().toString())) {
                txtNoOfFriesNursery.setError("Required");
                validationErrors.add("Required");
            }
            if (validateTor.isEmpty(txtNurseryDuration.getText().toString())) {
                txtNurseryDuration.setError("Required");
                validationErrors.add("Required");
            }
            if (validateTor.isEmpty(txtFinalAvgWeight.getText().toString())) {
                txtFinalAvgWeight.setError("Required");
                validationErrors.add("Required");
            }
         }

        return validationErrors.size() <= 0;
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
                            new OoOAlertDialog.Builder(ProductionDataActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Data successfully saved")
                                    .setAnimation(Animation.POP)
                                    .setPositiveButton("Ok", new OnClickListener() {
                                        @Override
                                        public void onClick() {
                                           onBackPressed();
                                        }
                                    })
                                    .setPositiveButtonColor(R.color.positive)
                                    .build();
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
