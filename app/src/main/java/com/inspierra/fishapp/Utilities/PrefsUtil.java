package com.inspierra.fishapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.inspierra.fishapp.HelpingClasses.LoginResponseClass;

public class PrefsUtil
{
    private static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences("APP_FishF", Context.MODE_PRIVATE);
    }

    public static void storeUserData(Context context, String userdata)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("USER_DATA", userdata);
        editor.apply();
    }

    public static void SetLoginStatus(Context context, boolean islogged)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("LOGGED_IN", islogged);
        editor.apply();
    }

    public static Boolean getLoggedIn(Context context)
    {
        return getSharedPreferences(context).getBoolean("LOGGED_IN",
                false);
    }

    public static void storeTempTokenData(Context context, String userdata)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("TOKEN", userdata);
        editor.apply();
    }

    public static String getTempTokenData(Context context)
    {
        String data = getSharedPreferences(context).getString("TOKEN",
                null);
        LoginResponseClass dd = new Gson().fromJson(data, LoginResponseClass.class);
        return dd.token;
    }

    public static LoginResponseClass getUserData(Context context)
    {
        String data = getSharedPreferences(context).getString("TOKEN",
                null);
        LoginResponseClass dd = new Gson().fromJson(data, LoginResponseClass.class);
        return dd;
    }
}
