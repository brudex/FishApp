package com.inspierra.fishapp.Utilities;

import com.inspierra.fishapp.HelpingClasses.AddPondRequestClass;
import com.inspierra.fishapp.HelpingClasses.AddPondResponseClass;
import com.inspierra.fishapp.HelpingClasses.FishHealthClass;
import com.inspierra.fishapp.HelpingClasses.FishSpeciesClass;
import com.inspierra.fishapp.HelpingClasses.LoginRequestClass;
import com.inspierra.fishapp.HelpingClasses.LoginResponseClass;
import com.inspierra.fishapp.HelpingClasses.ProfileClass;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorRequest;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorResponse;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveFishHealthResponseClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataRequestClass;
import com.inspierra.fishapp.HelpingClasses.SaveProductionDataResponseClass;
import com.inspierra.fishapp.HelpingClasses.SignupRequestClass;
import com.inspierra.fishapp.HelpingClasses.SignupResponseClass;
import com.inspierra.fishapp.HelpingClasses.UserPondsClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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

    @GET("/api/listdata/pondtypes")
    Call<String[]> GetPondTypes(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/login")
    Call<LoginResponseClass> Login(@Body LoginRequestClass data);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/user/updateprofile")
    Call<SaveProductionDataResponseClass> UpdateProfile(@Body ProfileClass data, @Header("Authorization") String auth);

    @GET("/api/user/getprofile")
    Call<ProfileClass> GetUserProfile(@Header("Authorization") String auth);

}
