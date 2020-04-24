package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorRequest;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorResponse;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;
import com.raywenderlich.android.validatetor.ValidateTor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

public class EconIndicator extends AppCompatActivity
{
    CustomEditText no_owrker, no_pond, amt_rec, amt_spnt;
    SaveEconomicIndicatorRequest reqq;
    LoadingButton btnLoad;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    //UserPondsClass pond;
    String filePath;
    int color;
    int requestCode = 0;
    RelativeLayout relRecord;
    RelativeLayout imgBack;
    ValidateTor validateTor = new ValidateTor();
    List<String> validationErrors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.econ_layout);
        validationErrors = new ArrayList<>();
        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        color = getResources().getColor(R.color.blueGray200);
        ActivityCompat.requestPermissions(this,
                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Data"); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        relRecord = findViewById(R.id.relRecord);
        no_owrker = findViewById(R.id.no_owrker);
        amt_rec = findViewById(R.id.amt_rec);
        amt_spnt = findViewById(R.id.amt_spnt);
        no_pond = findViewById(R.id.no_pond);
        btnLoad = findViewById(R.id.btnLoad);
        imgBack = findViewById(R.id.imgBack);
        reqq = new SaveEconomicIndicatorRequest();
        //reqq.pondId = pond.pondId;
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnLoad.setOnClickListener(v -> {
            if(validateFields()){
                reqq.moneyReceived = Integer.parseInt(amt_rec.getText().toString().trim());
                reqq.moneySpent = Integer.parseInt(amt_spnt.getText().toString().trim());
                reqq.noOfWorkers = Integer.parseInt(no_owrker.getText().toString().trim());
                reqq.numberOfPonds = Integer.parseInt(no_pond.getText().toString().trim());
                progressDialog.show();
                new SaveIndicator().execute();
            }
        });
        relRecord.setOnClickListener(v -> AndroidAudioRecorder.with(EconIndicator.this)
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


    private boolean validateFields(){
        validationErrors.clear();
        if (validateTor.isEmpty(amt_rec.getText().toString().trim())) {
            amt_rec.setError("The field is required");
            validationErrors.add("The field is required");
        }
        if (validateTor.isEmpty(amt_spnt.getText().toString().trim())) {
            amt_spnt.setError("The field is required");
            validationErrors.add("The field is required");
        }
        if (validateTor.isEmpty(no_owrker.getText().toString().trim())) {
            no_owrker.setError("The field is required");
            validationErrors.add("The field is required");
        }
        if (validateTor.isEmpty(no_pond.getText().toString().trim())) {
            no_pond.setError("The field is required");
            validationErrors.add("The field is required");
        }


        return validationErrors.size() <= 0;
    }


    class SaveIndicator extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SaveEconomicIndicatorResponse> saveIndicator = acquahService.SaveEconomicIndicator(reqq,
                    PrefsUtil.getTempTokenData(EconIndicator.this));
            saveIndicator.enqueue(new Callback<SaveEconomicIndicatorResponse>()
            {
                @Override
                public void onResponse(@NotNull Call<SaveEconomicIndicatorResponse> call,
                                       @NotNull Response<SaveEconomicIndicatorResponse> response)
                {
                    progressDialog.dismiss();
                    assert response.body() != null;
                    if (response.body().status.equals("00"))
                    {
                        //Toast.makeText(EconIndicator.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        new OoOAlertDialog.Builder(EconIndicator.this)
                                .setTitle("Success")
                                .setMessage("Data successfully saved")
                                .setAnimation(Animation.POP)
                                .setPositiveButton("Ok", null)
                                .setPositiveButtonColor(R.color.green)
                                .build();
                    }
                    else
                    {
                        if(response.body().message!=null){
                           Toast.makeText(EconIndicator.this, "Update Failed. Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<SaveEconomicIndicatorResponse> call, @NotNull Throwable t)
                {
                    progressDialog.dismiss();
                    Toast.makeText(EconIndicator.this, "Update Failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
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
