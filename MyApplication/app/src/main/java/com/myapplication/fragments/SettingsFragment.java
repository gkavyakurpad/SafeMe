package com.myapplication.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.myapplication.MainActivity;
import com.myapplication.R;
import com.myapplication.utils.AppPreference;
import com.myapplication.utils.AppUtils;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    private ImageButton primaryContactIcon;

    private ImageButton secondaryContactIcon;

    private TextView primaryNumber;
    private TextView secondaryNumber;

    private String number;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragment newInstance(int sectionNumber) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        primaryContactIcon = (ImageButton) rootView.findViewById(R.id.editIcon);
        secondaryContactIcon = (ImageButton) rootView.findViewById(R.id.editIcon2);
        primaryContactIcon.setOnClickListener(this);
        secondaryContactIcon.setOnClickListener(this);
        primaryNumber = (TextView) rootView.findViewById(R.id.primayContact);
        secondaryNumber = (TextView) rootView.findViewById(R.id.secondaryContact);

        ToggleButton notButton = (ToggleButton) rootView.findViewById(R.id.tg_switch_id);
        notButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AppUtils.getInstance().setNotificationStatus(getActivity(), isChecked);
                } else {
                    AppUtils.getInstance().setNotificationStatus(getActivity(), isChecked);
                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.editIcon:
                dialoToEnterPNumber();
                break;

            case R.id.editIcon2:
                dialoToEnterSNumber();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }


    public void dialoToEnterPNumber() {

        final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.setnumber);

        Button mBtnOkErrMsg = (Button) dialog.findViewById(R.id.btnOkCustomToast);
        mBtnOkErrMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText mTvErrorMsg = (EditText) dialog.findViewById(R.id.tvCustomToast);
                String num = mTvErrorMsg.getText().toString();

                primaryNumber.setText(num);
                AppPreference.getInstance(getActivity()).setPrimaryNumber(num);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialoToEnterSNumber() {

        final Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.setnumber);

        Button mBtnOkErrMsg = (Button) dialog.findViewById(R.id.btnOkCustomToast);
        mBtnOkErrMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText mTvErrorMsg = (EditText) dialog.findViewById(R.id.tvCustomToast);
                String num = mTvErrorMsg.getText().toString();
                secondaryNumber.setText(num);
                AppPreference.getInstance(getActivity()).setSeondaryNumber(num);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
