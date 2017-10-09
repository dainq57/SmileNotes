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

public class ProfileProblemFragment extends BaseFragment {
    private Context mContext;
    private CustomerObject mCustomer;
    private EditText mProblem;
    private EditText mSolution;
    private EditText mNote;
    private EditText mProduct;

    public ProfileProblemFragment(Context context) {
        mContext = context;
    }

    public ProfileProblemFragment(Context context, CustomerObject customer){
        mContext = context;
        mCustomer = customer;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_problem, null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdate();
    }

    private void initView(View view) {
        LinearLayout problemHeader = (LinearLayout) view.findViewById(R.id.create_header_problem);
        problemHeader.setVisibility(View.GONE);

        mProblem = (EditText) view.findViewById(R.id.create_edit_problem);
        mProblem.setEnabled(false);

        mSolution = (EditText)view.findViewById(R.id.create_edit_solution);
        mSolution.setEnabled(false);

        mNote = (EditText) view.findViewById(R.id.create_edit_note);
        mNote.setEnabled(false);

        mProduct = (EditText)view.findViewById(R.id.create_edit_product_need);
        mProduct.setEnabled(false);
    }

    private void onUpdate(){
        mProblem.setText(mCustomer.getProblem());
        mSolution.setText(mCustomer.getSolution());
        mNote.setText(mCustomer.getNote());
        mProduct.setText(mCustomer.getProduct());
    }
}
