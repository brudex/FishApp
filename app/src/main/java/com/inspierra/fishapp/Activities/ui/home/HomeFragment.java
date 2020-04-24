package com.inspierra.fishapp.Activities.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.inspierra.fishapp.Activities.HomePageActivity;
import com.inspierra.fishapp.Activities.SearchFarmsResultActivity;
import com.inspierra.fishapp.HelpingClasses.ProfileClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.FileUploadService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.model.Media;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.Objects;

import javax.xml.validation.Validator;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;
     TextView tvFullName;
    TextView txtPhone;
    TextView txtEmail;
    TextView txtLocation;
    androidx.cardview.widget.CardView cardNextMarket,cardNews;
    TextView tvAboutFarm;
    TextView tvNews;
    TextView tvFarmName;
    ProfileClass userProfile;
    ImageView ivEditProfilePic ;
    CircleImageView profile ;
    FileUploadService fileUploadService;
    AcquahService acquahService;
    static int ASYNC_ACTION = 0;
    private static final int ASYNC_FILE_UPLOAD = 1;
    private static final int ASYNC_PROFILE_PIC = 2;
    private static final int CAPTURE_MEDIA = 368;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        userProfile= PrefsUtil.getUserProfile(getActivity());
        tvFullName = root.findViewById(R.id.tvFullName);
        txtPhone = root.findViewById(R.id.txtPhone);
        txtEmail = root.findViewById(R.id.txtEmail);
        tvNews = root.findViewById(R.id.tvNews);
        txtLocation = root.findViewById(R.id.txtLocation);
        tvFarmName = root.findViewById(R.id.tvFarmName);
        ivEditProfilePic = root.findViewById(R.id.ivEditProfilePic);
        cardNextMarket = root.findViewById(R.id.cardNextMarket);
        cardNews = root.findViewById(R.id.cardNews);
        profile = root.findViewById(R.id.profile);
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        fileUploadService= new FileUploadService(getActivity(),acquahService);
        if(userProfile!=null){
            tvFullName.setText(userProfile.getFullName().toUpperCase());
            txtPhone.setText(userProfile.phoneNumber);
            txtEmail.setText(userProfile.email);
            txtLocation.setText(capitalizeAllFirstLetter(userProfile.location));
            if(userProfile.farmName!=null){
                tvFarmName.setText(userProfile.farmName);
                tvAboutFarm.setText(userProfile.farmName);
            }
            if(userProfile.aboutFarm!=null){
                tvAboutFarm.setText(userProfile.aboutFarm);
            }
            tvNews.setText("Corona virus is real stay safe.\nAre you staying safe ?");
        }
        cardNextMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchFarmsResultActivity.class));
            }
        });
        cardNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OoOAlertDialog.Builder(getActivity())
                        .setTitle("Attention!!")
                        .setMessage(tvNews.getText().toString())
                        .setAnimation(Animation.POP)
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .setNegativeButtonColor(R.color.blue)
                        .setPositiveButtonColor(R.color.green)
                        .build();
            }
        });
        ivEditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchProfileCamera();
            }
        });
        setActionBarTitle("Home");
        if(!new ValidateTor().isEmpty(userProfile.profilePicture)){
            Picasso.get().load(userProfile.profilePicture).into(profile);
        }
        return root;
    }

    private void launchProfileCamera(){
        SandriosCamera
                .with()
                .setShowPicker(true)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                .enableImageCropping(true)
                .launchCamera(Objects.requireNonNull(getActivity()));
    }

    private  ProgressDialog getProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        return  progressDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == SandriosCamera.RESULT_CODE
                && data != null) {
            if (data.getSerializableExtra(SandriosCamera.MEDIA) instanceof Media) {
                Media media = (Media) data.getSerializableExtra(SandriosCamera.MEDIA);
                File file = new File(media.getPath());
                Picasso.get().load("file://"+media.getPath()).into(profile);
                Log.e("ProfilePicFile", "" + media.getPath());
                Log.e("ProfilePicFile", "" + media.getType());
                fileUploadService.setTag("profilepic");
                fileUploadService.setUserId(userProfile.userId);
                fileUploadService.setFile(file);
                Toast.makeText(getActivity(), "Media captured.", Toast.LENGTH_SHORT).show();
                ASYNC_ACTION = ASYNC_FILE_UPLOAD;
                new UploadIndicator().execute();

            }
        }
    }

    private void setActionBarTitle(String title){
        ((HomePageActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(title);
    }

    String capitalizeAllFirstLetter(String name) {
        char[] array = name.toCharArray();
        array[0] = Character.toUpperCase(array[0]);

        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }
        return new String(array);
    }

    class UploadIndicator extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {

            if(ASYNC_ACTION==ASYNC_FILE_UPLOAD){
                fileUploadService.uploadFile(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getActivity(), "Profile picture successfully updated.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error. Failed to update profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(ASYNC_ACTION == ASYNC_PROFILE_PIC){
               // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
             }


            return null;
        }
    }
}
