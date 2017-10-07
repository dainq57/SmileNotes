package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dainq.smilenotes.common.BaseFragment;

import nq.dai.smilenotes.R;

public class ProfileProblemFragment extends BaseFragment {
    private Context mContext;

    public ProfileProblemFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_problem, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        LinearLayout headerProblem = (LinearLayout) view.findViewById(R.id.create_header_problem);
        headerProblem.setVisibility(View.GONE);
    }
}
