package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.inspierra.fishapp.HelpingClasses.ProfileClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataResponseClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.libizo.CustomEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity
{
    CustomEditText txtfname, txtlname, txtmname, txtemail, txtpass, txtphone, txtfarmname, txtlocation, cages, ponds,
            hatcheries;
    Spinner region;
    LoadingButton btnUpdate;
    TextView txtFarmname;

    ProgressDialog progressDialog;
    AcquahService acquahService;
    ProfileClass profile;
    DatePicker mktDate;
    RelativeLayout relback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        profile = new ProfileClass();

        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

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
          /* setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);*/
        }

        relback = findViewById(R.id.relback);
        relback.setOnClickListener(v -> onBackPressed());
        mktDate = findViewById(R.id.marketDate);
        txtfname = findViewById(R.id.txtfname);
        txtlname = findViewById(R.id.txtlname);
        txtmname = findViewById(R.id.txtmname);
        txtemail = findViewById(R.id.txtEmail);
        txtpass = findViewById(R.id.txtPass);
        txtphone = findViewById(R.id.txtPhone);
        txtfarmname = findViewById(R.id.txtFarmname);
        btnUpdate = findViewById(R.id.btnSignup);
        region = findViewById(R.id.spnRegions);
        txtlocation = findViewById(R.id.txtlocation);
        btnUpdate = findViewById(R.id.btnUpdate);
        hatcheries = findViewById(R.id.hatchery);
        cages = findViewById(R.id.cages);


        btnUpdate.setOnClickListener(v -> {
            profile.email = Objects.requireNonNull(txtemail.getText()).toString().trim();
            profile.middleName = Objects.requireNonNull(txtmname.getText()).toString().trim();
            profile.lastName = Objects.requireNonNull(txtlname.getText()).toString().trim();
            profile.phoneNumber = Objects.requireNonNull(txtphone.getText()).toString().trim();
            profile.farmName = Objects.requireNonNull(txtfarmname.getText()).toString().trim();
            profile.location = Objects.requireNonNull(txtlocation.getText()).toString().trim();
            profile.noOfCages = Integer.parseInt(Objects.requireNonNull(cages.getText()).toString().trim());
            profile.noOfHatcheries = Integer.parseInt(Objects.requireNonNull(hatcheries.getText()).toString().trim());
            profile.noOfPonds = Integer.parseInt(Objects.requireNonNull(ponds.getText()).toString().trim());

            progressDialog.show();
            new updateProfilr().execute();
        });

        progressDialog.show();
        new getDetails().execute();
    }


    class getDetails extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<ProfileClass> details =
                    acquahService.GetUserProfile(PrefsUtil.getTempTokenData(ProfileActivity.this));
            details.enqueue(new Callback<ProfileClass>()
            {
                @Override
                public void onResponse(@NotNull Call<ProfileClass> call, @NotNull Response<ProfileClass> response)
                {
                    progressDialog.dismiss();
                    try
                    {
                        assert response.body() != null;
                        if (response.body().firstName.isEmpty())
                        {
                            Toast.makeText(ProfileActivity.this, "Could not fetch profile data.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            runOnUiThread(() -> {
                                txtfname.setText(response.body().firstName);
                                txtlname.setText(response.body().lastName);
                                txtmname.setText(response.body().middleName);
                                txtemail.setText(response.body().email);
                                txtphone.setText(response.body().phoneNumber);
                                txtfarmname.setText(response.body().farmName);
                                txtlocation.setText(response.body().location);
                                hatcheries.setText(response.body().noOfHatcheries);
                                cages.setText(response.body().noOfCages);
                                ponds.setText(String.valueOf(response.body().noOfPonds));

                            });
                        }
                    }
                    catch (Exception ex)
                    {
                        String error = ex.getMessage();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ProfileClass> call, @NotNull Throwable t)
                {
                    Toast.makeText(ProfileActivity.this, "Could not fetch profile data.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

    class updateProfilr extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<SaveProductionDataResponseClass> updateProfile = acquahService.UpdateProfile(profile,
                    PrefsUtil.getTempTokenData(ProfileActivity.this));
            updateProfile.enqueue(new Callback<SaveProductionDataResponseClass>()
            {
                @Override
                public void onResponse(Call<SaveProductionDataResponseClass> call,
                                       Response<SaveProductionDataResponseClass> response)
                {
                    assert response.body() != null;
                    if (response.body().status.equals("00"))
                    {
                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this, "Updated failed.Please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SaveProductionDataResponseClass> call, Throwable t)
                {
                    Toast.makeText(ProfileActivity.this, "Updated failed.Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}
