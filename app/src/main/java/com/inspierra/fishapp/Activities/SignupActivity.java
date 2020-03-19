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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.HelpingClasses.SignupRequestClass;
import com.inspierra.fishapp.HelpingClasses.SignupResponseClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity
{
    CustomEditText txtfname, txtlname, txtemail, txtpass, txtphone, txtfarmname, txtlocation, txtregion, txtPassCon;
    LoadingButton btnSignup;
    SignupRequestClass sdata;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    RelativeLayout imgback;
    Spinner spn_region;
    OoOAlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        sdata = new SignupRequestClass();
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Completing Registration"); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        spn_region = findViewById(R.id.spnRegions);

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
            /*  setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);*/
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        imgback = findViewById(R.id.imgback);
        txtfname = findViewById(R.id.txtfname);
        txtlname = findViewById(R.id.txtlname);
         txtemail = findViewById(R.id.txtEmail);
        txtpass = findViewById(R.id.txtPass);
        txtphone = findViewById(R.id.txtPhone);
        txtfarmname = findViewById(R.id.txtFarmname);
        btnSignup = findViewById(R.id.btnSignup);
        txtPassCon = findViewById(R.id.txtPassCon);

        txtlocation = findViewById(R.id.txtlocation);

        btnSignup.setOnClickListener(v -> {
            if (txtfname.getText().toString().trim().equals("")
                    || txtlname.getText().toString().trim().equals("")
                     || txtemail.getText().toString().trim().equals("") ||
                    txtpass.getText().toString().trim().equals("")
                    || txtphone.getText().toString().trim().equals("") ||
                    txtfarmname.getText().toString().trim().equals("")
                    || txtlocation.getText().toString().trim().equals(""))
            {
                Toast.makeText(SignupActivity.this, "Please provide all information",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!txtpass.getText().toString().trim().
                    equals(Objects.requireNonNull(txtPassCon.getText()).toString().trim()))
            {
                Toast.makeText(SignupActivity.this, "Passwords do not match",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                sdata.email = txtemail.getText().toString().trim();
                sdata.farmName = txtfarmname.getText().toString().trim();
                sdata.firstName = txtfname.getText().toString().trim();
                sdata.lastName = txtlname.getText().toString().trim();
                sdata.phoneNumber = txtphone.getText().toString().trim();
                sdata.password = txtpass.getText().toString().trim();
                sdata.location = txtlocation.getText().toString().trim();
                sdata.region = spn_region.getSelectedItem().toString();
                progressDialog.show();
                new doSignup().execute();
            }
        });

        imgback.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    class doSignup extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SignupResponseClass> signup = acquahService.SignUp(sdata);
            signup.enqueue(new Callback<SignupResponseClass>()
            {
                @Override
                public void onResponse(@NotNull Call<SignupResponseClass> call,
                                       @NotNull Response<SignupResponseClass> response)
                {
                    try
                    {
                        progressDialog.dismiss();
                        assert response.body() != null;
                        if (response.body().status.equals("00"))
                        {
                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Registration completed")
                                    .setPositiveButton("continue", (dialog, which) -> startActivity(
                                            new Intent(SignupActivity.this, LoginActivity.class)))
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                        else
                        {
                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Registration failed")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("retry", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                    catch (Exception ex)
                    {
                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("Error")
                                .setMessage("Registration failed")
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("retry", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SignupResponseClass> call, @NotNull Throwable t)
                {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("Error")
                            .setMessage("Registration failed")
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("retry", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            return null;
        }
    }
}
