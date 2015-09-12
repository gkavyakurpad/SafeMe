package com.myapplication.utils;

import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CallLog;


import com.myapplication.constants.AppConstant;


public class AppUtils {
    private AlertDialog alert;
    private static AppUtils util;


    private AppUtils() {

    }

    public static AppUtils getInstance() {
        if (util == null) {
            util = new AppUtils();
        }
        return util;
    }


    /**
     * Checks that application runs first time and write flag at
     * SharedPreferences
     *
     * @return true if 1st time
     */
    public  boolean getNotificationStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                AppConstant.NOTIFICATION_CONST.NOTIFICATION_STATUS, Context.MODE_PRIVATE);

        boolean ranBefore = preferences.getBoolean(AppConstant.NOTIFICATION_CONST.NOTIFICATION_STATUS,
                false);
        return !ranBefore;
    }

    /**
     * Method to set app launched first time flag.
     */
    public void setNotificationStatus(Context context,boolean status) {
        // on accepting the license agreement , license activity will not be
        // launched for second time.
        SharedPreferences preferences = context.getSharedPreferences(
                AppConstant.NOTIFICATION_CONST.NOTIFICATION_STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AppConstant.NOTIFICATION_CONST.NOTIFICATION_STATUS, status);
        editor.commit();
    }


    /**
     * Check for any type of data connection
     *
     * @param context
     * @return true if any of 3G, GPRS, WiFi or WiMAX is available else false
     */
    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public String retriveCallSummary(Context context) {
        String phNumber = null;
        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = context.getContentResolver().query(
                contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int duration1 = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        if( managedCursor.moveToLast() == true ) {
            phNumber = managedCursor.getString( number );

        }
        managedCursor.close();
        return phNumber;
    }
}
