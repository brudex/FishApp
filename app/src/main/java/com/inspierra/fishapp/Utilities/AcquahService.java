package com.inspierra.fishapp.Utilities;

import com.inspierra.fishapp.HelpingClasses.AddPondRequestClass;
import com.inspierra.fishapp.HelpingClasses.AddPondResponseClass;
import com.inspierra.fishapp.HelpingClasses.EconIndicatorOutputResponse;
import com.inspierra.fishapp.HelpingClasses.FarmTipsClass;
import com.inspierra.fishapp.HelpingClasses.FishHealthClass;
import com.inspierra.fishapp.HelpingClasses.FishHealthOutputResponse;
import com.inspierra.fishapp.HelpingClasses.FishSpeciesClass;
import com.inspierra.fishapp.HelpingClasses.LoginRequestClass;
import com.inspierra.fishapp.HelpingClasses.LoginResponseClass;
import com.inspierra.fishapp.HelpingClasses.OutPutRequestClass;
import com.inspierra.fishapp.HelpingClasses.ProductionDataOutput;
import com.inspierra.fishapp.HelpingClasses.ProfileClass;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorRequest;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorResponse;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthResponseClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataResponseClass;
import com.inspierra.fishapp.HelpingClasses.SearchFarmersRequest;
import com.inspierra.fishapp.HelpingClasses.SearchResponseClass;
import com.inspierra.fishapp.HelpingClasses.SignupRequestClass;
import com.inspierra.fishapp.HelpingClasses.SignupResponseClass;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AcquahService
{
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/signup")
    Call<SignupResponseClass> SignUp(@Body SignupRequestClass data);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/addpond")
    Call<AddPondResponseClass> AddPond(@Body AddPondRequestClass data, @Header("Authorization") String auth);

    @GET("/api/user/myponds")
    Call<ArrayList<UserPondsClass>> GetUserPonds(@Header("Authorization") String auth);



    @GET("/api/farmtips/getlatest")
    Call<ArrayList<FarmTipsClass>> GetFarmTips(@Header("Authorization") String auth);

    @GET("/api/checkrecord/fishhealth")
    Call<FishHealthClass> GetFishHealth(@Body FishHealthClass data, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/checkrecord/economicindicator")
    Call<SaveEconomicIndicatorResponse> SaveEconomicIndicator(@Body SaveEconomicIndicatorRequest data,
                                                              @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/checkrecord/fishhealth")
    Call<SaveFishHealthResponseClass> SaveFishHealth(@Body SaveFishHealthRequestClass data, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/checkrecord/productiondata")
    Call<SaveProductionDataResponseClass> SaveProductionData(@Body SaveProductionDataRequestClass data,
                                                             @Header("Authorization") String auth);

    @GET("/api/checkrecord/productiondata")
    Call<SaveProductionDataRequestClass> GetProductionData(@Body SaveProductionDataRequestClass data,
                                                           @Header("Authorization") String auth);

    @GET("/api/listdata/fishspecies")
    Call<ArrayList<FishSpeciesClass>> GetFishSpecies(@Header("Authorization") String auth);

    @POST("/api/admin/farm/economicindicators")
    Call<EconIndicatorOutputResponse> EconomicIndicatorsOutput(@Body OutPutRequestClass data, @Header("Authorization") String auth);


    @POST("/api/admin/pond/productiondata")
    Call<ProductionDataOutput> ProductionDataOutput(@Body OutPutRequestClass data, @Header("Authorization") String auth);


    @POST("/api/admin/search/farmers")
    Call<SearchResponseClass> FarmSearch(@Body SearchFarmersRequest data, @Header("Authorization") String auth);


    @POST("/api/admin/pond/fishhealth")
    Call<FishHealthOutputResponse> FishHealthOutput(@Body OutPutRequestClass data, @Header("Authorization") String auth);


    @GET("/api/listdata/pondtypes")
    Call<List<String>> GetPondTypes(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/login")
    Call<LoginResponseClass> Login(@Body LoginRequestClass data);
    @Headers({"Content-Type: application/json;charset=UTF-8"})

    @POST("/api/user/forgotpassword")
    Call<SignupResponseClass> ForgotPassword(@Body LoginRequestClass data);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/updateprofile")
    Call<SaveProductionDataResponseClass> UpdateProfile(@Body ProfileClass data, @Header("Authorization") String auth);

    @GET("/api/user/getprofile")
    Call<ProfileClass> GetUserProfile(@Header("Authorization") String auth);

    @Multipart
    @POST("/uploadfile")
    Call<ResponseBody> upload(
            @Part("userId") RequestBody userId,
            @Part("tag") RequestBody tag,
            @Part MultipartBody.Part file
    );


}
