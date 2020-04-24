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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.FishHealthClass;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthResponseClass;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.BusStation;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
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

public class FishHealthActivity extends AppCompatActivity
{
    ProgressDialog progressDialog;
    AcquahService acquahService;
    SaveFishHealthRequestClass requestClass;
    FishHealthClass reqq;
    ImageView FishDisease;

    LoadingButton btnSubmit;
    CustomEditText pondId,colorOfWater, mortality, phLevel,
            Oxygen, Temperature, Ammonia, Nitrite, Turbidity;
    UserPondsClass pond;
    ImageView waterPicture;

    private String[] permissions = {Manifest.permission.CAMERA};

    RelativeLayout relRecord;
    String filePath;
    int color;
    int requestCode = 0;
    RelativeLayout relBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_health_layout);
        Intent intent = getIntent();
        pond = new Gson().fromJson(intent.getStringExtra("pond"), UserPondsClass.class);

        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";

//        ActivityCompat.requestPermissions(this,
//                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);

//        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Data"); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
//
//        if (Build.VERSION.SDK_INT >= 21)
//        {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//          /* setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);*/
//        }
//
        relBack = findViewById(R.id.relBack);
        relRecord = findViewById(R.id.relRecord);
//        pondId = findViewById(R.id.pondId);
        colorOfWater = findViewById(R.id.colorOfWater);
        waterPicture = findViewById(R.id.waterPicture);
        FishDisease = findViewById(R.id.FishDisease);
        mortality = findViewById(R.id.mortality);
        phLevel = findViewById(R.id.phLevel);
        Oxygen = findViewById(R.id.Oxygen);
        Temperature = findViewById(R.id.Temperature);
        Ammonia = findViewById(R.id.Ammonia);
        Nitrite = findViewById(R.id.Nitrite);
        Turbidity = findViewById(R.id.Turbidity);
        btnSubmit = findViewById(R.id.btnSubmit);
        relBack.setOnClickListener(v -> {
            onBackPressed();
        });
        FishDisease.setOnClickListener(v ->
        {
            if (ActivityCompat.checkSelfPermission(FishHealthActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        });
        waterPicture.setOnClickListener(v ->
        {
            if (ActivityCompat.checkSelfPermission(FishHealthActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        });
        try
        {
            if (pond.pondId > 0)
            {
                reqq = new FishHealthClass();
                reqq.pondId = pond.pondId;
                //progressDialog.show();
                //new getFishHealth().execute();
            }
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }

        btnSubmit.setOnClickListener(v -> {
            progressDialog.show();
            requestClass = new SaveFishHealthRequestClass();
            requestClass.pondId = pond.pondId;
            requestClass.colorOfWater = colorOfWater.getText().toString().trim();
            requestClass.mortality = mortality.getText().toString().trim();
            requestClass.phLevel = Integer.parseInt(phLevel.getText().toString().trim());
            requestClass.dissolvedOxygen = Integer.parseInt(Oxygen.getText().toString().trim());
            requestClass.ammonia = Integer.parseInt(Ammonia.getText().toString().trim());
            requestClass.nitrite = Integer.parseInt(Nitrite.getText().toString().trim());
            requestClass.turbidity = Integer.parseInt(Turbidity.getText().toString().trim());
            new saveHealth().execute();
        });

        relRecord.setOnClickListener(v -> AndroidAudioRecorder.with(FishHealthActivity.this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)
                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)
                // Start recording
                .record());

    }

    class saveHealth extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SaveFishHealthResponseClass> saveFish = acquahService.SaveFishHealth(requestClass,
                    PrefsUtil.getTempTokenData(FishHealthActivity.this));
            saveFish.enqueue(new Callback<SaveFishHealthResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SaveFishHealthResponseClass> call,
                                       @NotNull Response<SaveFishHealthResponseClass> response)
                {
                    progressDialog.dismiss();
                    assert response.body() != null;
                    try
                    {
                        if (response.body().status.equals("00"))
                        {
                            new OoOAlertDialog.Builder(FishHealthActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Data successfully saved")
                                    .setAnimation(Animation.POP)
                                    .setPositiveButton("Ok", null)
                                    .setPositiveButtonColor(R.color.green)
                                    .build();
                        }
                        else
                        {
                            Toast.makeText(FishHealthActivity.this, "Fish Data Add Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(FishHealthActivity.this, "Fish Data Add Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SaveFishHealthResponseClass> call, Throwable t)
                {
                    progressDialog.dismiss();
                    Toast.makeText(FishHealthActivity.this, "Fish Data Add Failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

    class getFishHealth extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<FishHealthClass> getFishHealth = acquahService.GetFishHealth(reqq,
                    PrefsUtil.getTempTokenData(FishHealthActivity.this));
            getFishHealth.enqueue(new Callback<FishHealthClass>()
            {
                @Override
                public void onResponse(@NotNull Call<FishHealthClass> call, @NotNull Response<FishHealthClass> response)
                {
                    progressDialog.dismiss();
                    assert response.body() != null;
                    if (response.body().pondId > 0)
                    {
                        try
                        {
                               runOnUiThread(() -> {
                               // colorOfWater.setText(response.body().colorOfWater);
                               // WaterPicture.setText(response.body().waterPictureLink);
                                mortality.setText(response.body().mortality);
                                phLevel.setText(response.body().phLevel);
                                Oxygen.setText(response.body().dissolvedOxygen);
                                Temperature.setText(response.body().temperature);
                                Ammonia.setText(response.body().ammonia);
                                Nitrite.setText(response.body().nitrite);
                                Turbidity.setText(response.body().turbidity);
                            });
                        }
                        catch (Exception ex)
                        {
                            String error = ex.getMessage();
                            Toast.makeText(FishHealthActivity.this, "Error loading data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(FishHealthActivity.this, "Error loading data",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FishHealthClass> call, Throwable t)
                {
                    progressDialog.dismiss();
                    Toast.makeText(FishHealthActivity.this, "Error loading data",
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
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
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
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Glide.with(this).load(photo).into(waterPicture);
        }

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
