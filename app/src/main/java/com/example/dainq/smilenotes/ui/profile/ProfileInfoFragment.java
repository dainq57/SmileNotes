package com.example.dainq.smilenotes.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;

import nq.dai.smilenotes.R;


public class ProfileInfoFragment extends Fragment {
    private Context mContext;
    private TextView mDateOfBirth;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mReason;
    private RadioButton mMale, mFemale;
    private EditText mJob;

    private CustomerRequest mCustomer;

    public ProfileInfoFragment(Context context) {
        mContext = context;
    }

    public ProfileInfoFragment(Context context, CustomerRequest object) {
        mContext = context;
        mCustomer = object;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdate();
    }

    private void onUpdate() {
        String dob = mCustomer.getDateOfBirth();
        if (dob != null) {
            mDateOfBirth.setText(dob);
        }
        mPhoneNumber.setText(mCustomer.getPhone());
        mAddress.setText(mCustomer.getAddress());
        mReason.setText(mCustomer.getReason());
        mJob.setText(mCustomer.getJob());
        int gender = mCustomer.getGender();
        if (gender == 0) {
            mFemale.setChecked(true);
        } else {
            mMale.setChecked(true);
        }
    }

    private void initView(View view) {
        LinearLayout profileFilter = (LinearLayout) view.findViewById(R.id.create_filter);
        profileFilter.setVisibility(View.GONE);

        LinearLayout infoHeader = (LinearLayout) view.findViewById(R.id.create_header_info);
        infoHeader.setVisibility(View.GONE);

        LinearLayout layoutName = (LinearLayout) view.findViewById(R.id.create_name);
        layoutName.setVisibility(View.GONE);

        mDateOfBirth = (TextView) view.findViewById(R.id.create_edit_date_of_birth);
        mPhoneNumber = (EditText) view.findViewById(R.id.create_edit_phonenumber);
        mPhoneNumber.setEnabled(false);
        mAddress = (EditText) view.findViewById(R.id.create_edit_address);
        mAddress.setEnabled(false);
        mReason = (EditText) view.findViewById(R.id.create_edit_reason);
        mReason.setEnabled(false);
        mJob = (EditText) view.findViewById(R.id.create_edit_job);
        mJob.setEnabled(false);

        RadioGroup mGender = (RadioGroup) view.findViewById(R.id.create_gender);
        mMale = (RadioButton) mGender.findViewById(R.id.create_gender_male);
        mFemale = (RadioButton) mGender.findViewById(R.id.create_gender_female);
        mMale.setClickable(false);
        mFemale.setClickable(false);
    }
}
