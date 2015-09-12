package com.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.myapplication.constants.AppConstant;


public class AppPreference {

    private SharedPreferences appSharedPrefs;
       private Context mContext;


    private static AppPreference sInstance;

    private AppPreference(Context context) {
        mContext = context.getApplicationContext();
        this.appSharedPrefs = mContext.getSharedPreferences(
                AppConstant.PREF_CONST.PREF, Activity.MODE_PRIVATE);

    }

    public static synchronized AppPreference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreference(context);
        }
        return sInstance;
    }

    public String getPrimaryNumber() {
        return appSharedPrefs.getString("PRIMARY_NUMBER","");
    }

    public void setPrimaryNumber(String primaryNumber) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("PRIMARY_NUMBER", primaryNumber).commit();
    }

    public String getSecondaryNumber() {
        return appSharedPrefs.getString("SECONDARY_NUMBER","");
    }

    public void setSeondaryNumber(String seondaryNumber) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("SECONDARY_NUMBER", seondaryNumber).commit();
    }


}
