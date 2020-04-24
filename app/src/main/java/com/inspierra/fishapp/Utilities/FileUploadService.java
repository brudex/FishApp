package com.inspierra.fishapp.Utilities;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadService {

    AcquahService service;
    Context context;
    File mFile;
    HashMap<String,String> fields;
    public FileUploadService(Context mContext, AcquahService  acquahService){
          service = acquahService ;
          context=mContext;
          fields= new HashMap<>();
    }

    public void setFile(File file){
        mFile = file;
    }
    public void setTag(String tag){
        fields.put("tag",tag);
    }
    public void setUserId(String userid){
        fields.put("userid",userid);
    }

    public void uploadFile(Callback<ResponseBody> uploadStatus) {
        // create upload service client
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
         // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), mFile);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", mFile.getName(), requestFile);
        // add another part within the multipart request
        RequestBody tag = RequestBody.create( okhttp3.MultipartBody.FORM, fields.get("tag").toString());
        RequestBody userid =  RequestBody.create(okhttp3.MultipartBody.FORM, fields.get("userid").toString());
        // finally, execute the request
        Call<ResponseBody> call = service.upload(userid,tag, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                uploadStatus.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                 uploadStatus.onFailure(call,t);
            }
        });
    }
}
