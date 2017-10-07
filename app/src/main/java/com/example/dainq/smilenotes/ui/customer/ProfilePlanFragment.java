package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.BaseFragment;

import nq.dai.smilenotes.R;

public class ProfilePlanFragment extends BaseFragment {
    private Context mContext;

    public ProfilePlanFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_plan, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}
