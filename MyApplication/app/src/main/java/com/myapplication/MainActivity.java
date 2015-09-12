package com.myapplication;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.myapplication.Interface.AccelerometerListener;
import com.myapplication.constants.AppConstant;
import com.myapplication.fragments.EmergencyInfoFragment;
import com.myapplication.fragments.HomeFragment;
import com.myapplication.fragments.MapFragment;
import com.myapplication.fragments.SettingsFragment;
import com.myapplication.service.AccelerometerManager;
import com.myapplication.service.LocationService;
import com.myapplication.utils.AppPreference;
import com.myapplication.utils.AppUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AccelerometerListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mCurrentSelectedPosition = 0;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private LocationService locationTracker;

    private double latitude;
    private double longitude;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        locationTracker = new LocationService(this);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
//        Toast.makeText(this, "Example action."+position, Toast.LENGTH_SHORT).show();
        Log.d("Tag", "Postion: " + position);
        switch (position) {
            case AppConstant.NAVI_DRAW_TOPLIST.NOP:
                break;
            case AppConstant.NAVI_DRAW_TOPLIST.HOME: {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance(AppConstant.NAVI_DRAW_TOPLIST.HOME))
                        .commit();

                mCurrentSelectedPosition = position;
                if (mNavigationDrawerFragment != null) {
                    mNavigationDrawerFragment.setCurrentPosition(mCurrentSelectedPosition);
                }
                /// AgeValidatingActivity.first = true;

            }

            break;
            case AppConstant.NAVI_DRAW_TOPLIST.MAP:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MapFragment.newInstance(AppConstant.NAVI_DRAW_TOPLIST.MAP))
                        .commit();
                mCurrentSelectedPosition = position;
                mNavigationDrawerFragment.setCurrentPosition(mCurrentSelectedPosition);
                break;

            case AppConstant.NAVI_DRAW_TOPLIST.SETTING:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance(AppConstant.NAVI_DRAW_TOPLIST.SETTING))
                        .commit();
                mCurrentSelectedPosition = position;
                mNavigationDrawerFragment.setCurrentPosition(mCurrentSelectedPosition);
                break;

            case AppConstant.NAVI_DRAW_TOPLIST.EMERGENCY_INFO:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, EmergencyInfoFragment.newInstance(AppConstant.NAVI_DRAW_TOPLIST.EMERGENCY_INFO))
                        .commit();
                mCurrentSelectedPosition = position;
                mNavigationDrawerFragment.setCurrentPosition(mCurrentSelectedPosition);
                break;

        }

    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.important_info);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();
        return true;
        //  }
        //  return super.onCreateOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    System.out.println("ACTION UP DIS");
                    String recentCaller = AppUtils.getInstance().retriveCallSummary(this);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+recentCaller));
                    startActivity(intent);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    System.out.println("ACTION DOWN DIS");
                    String primaryNum = AppPreference.getInstance(this).getPrimaryNumber();
                    String seondaryNum = AppPreference.getInstance(this).getSecondaryNumber();
                    if(locationTracker.canGetLocation()) {
                        latitude = locationTracker.getLatitude();
                        longitude = locationTracker.getLongitude();
                    } else {
                        locationTracker.showSettingsAlert();
                    }
                    if(latitude != 0 && longitude!= 0 ) {
                        message = getString(R.string.current_location)+"" +"http://maps.google.com/?q="+latitude+","+longitude;
                    }

                    sendSMS(primaryNum,getString(R.string.message)+message);
                    sendSMS(seondaryNum,getString(R.string.message)+message);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }


    public void onShake(float force) {

    //    if (AppUtils.getInstance().getNotificationStatus(this)) {
            Toast.makeText(getBaseContext(), "Motion detected",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:123456789"));
            startActivity(intent);
       // }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {


//                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
}
