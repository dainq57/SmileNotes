package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.model.CustomerObject;

import nq.dai.smilenotes.R;

public class ProfileInfoFragment extends BaseFragment {
    private Context mContext;
    private TextView mDateOfBirth;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mReason;

    private CustomerObject mCustomer;

    public ProfileInfoFragment(Context context) {
        mContext = context;
    }

    public ProfileInfoFragment(Context context, CustomerObject object) {
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
        mDateOfBirth.setText(mCustomer.getDateofbirth());
        mPhoneNumber.setText(mCustomer.getPhonenumber());
        mAddress.setText(mCustomer.getAddress());
        mReason.setText(mCustomer.getReason());
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
    }
}
