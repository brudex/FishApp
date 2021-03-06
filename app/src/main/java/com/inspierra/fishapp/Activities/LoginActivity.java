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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    CustomEditText username, password;
    LoadingButton btnSubmit;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    LoginRequestClass requestClass;
    TextView tvForgotPassword;
    RelativeLayout imgback;
    ValidateTor validateTor = new ValidateTor();
    List<String> validationErrors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        validationErrors=new ArrayList<>();
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

        username = findViewById(R.id.txtfname);
        password = findViewById(R.id.txtmname);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        imgback = findViewById(R.id.imgback);

        btnSubmit.setOnClickListener(v -> {
            requestClass = new LoginRequestClass();
            requestClass.password = Objects.requireNonNull(password.getText()).toString().trim();
            requestClass.userName = Objects.requireNonNull(username.getText()).toString().trim();

            if (validateFields())
            {
                progressDialog.show();
                new doLogin().execute();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login information required", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this,ForgotPasswordActivity.class));
        });

        imgback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private boolean validateFields(){
        validationErrors.clear();
        if (validateTor.isEmpty(requestClass.userName)) {
            username.setError("Email or phone number is required");
            validationErrors.add("Email or phone number is required");
        }
        if (validateTor.isEmpty(requestClass.password)) {
            password.setError("Password is required");
            validationErrors.add("Password is required");
        }
        return validationErrors.size() <= 0;
    }

    class doLogin extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<LoginResponseClass> login = acquahService.Login(requestClass);
            login.enqueue(new Callback<LoginResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<LoginResponseClass> call, @NotNull Response<LoginResponseClass> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if (response.body().success)
                        {
                            PrefsUtil.storeTempTokenData(LoginActivity.this, new Gson().toJson(response.body()));
                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                            LoginActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Login failed. Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Login failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseClass> call, Throwable t)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Login failed. Try again", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}
