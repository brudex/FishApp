package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.LoginRequestClass;
import com.inspierra.fishapp.HelpingClasses.LoginResponseClass;
import com.inspierra.fishapp.HelpingClasses.SignupResponseClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity
{
    CustomEditText txtEmailPhone;
    LoadingButton btnSubmit;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    LoginRequestClass requestClass;

    RelativeLayout imgback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);

        if (Build.VERSION.SDK_INT < 21)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
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
            //*  setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);*//*
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        txtEmailPhone = findViewById(R.id.txtEmailPhone);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgback = findViewById(R.id.imgBack);
        btnSubmit.setOnClickListener(v -> {
            requestClass = new LoginRequestClass();
            requestClass.userName= Objects.requireNonNull(txtEmailPhone.getText()).toString();
            if(!requestClass.userName.equals("")){
                new doLogin().execute();
                progressDialog.show();
            }else{
                Toast.makeText(getApplicationContext(),"Please enter a valid email or phone number",Toast.LENGTH_LONG).show();
            }
        });

        imgback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    class doLogin extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SignupResponseClass> login = acquahService.ForgotPassword(requestClass);
            login.enqueue(new Callback<SignupResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SignupResponseClass> call, @NotNull Response<SignupResponseClass> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;

                        new OoOAlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle("")
                                .setMessage(response.body().message)
                                .setAnimation(Animation.POP)
                                .setPositiveButton("Ok", null)
                                .build();
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "Request failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SignupResponseClass> call, Throwable t)
                {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Request failed. Try again", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}
