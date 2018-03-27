package com.example.dainq.smilenotes.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;

import nq.dai.smilenotes.R;

public class ProfileProblemFragment extends Fragment {
    private Context mContext;
    private CustomerRequest mCustomer;
    private EditText mProblem;
    private EditText mSolution;
    private EditText mNote;
    private EditText mProduct;

    public ProfileProblemFragment(Context context) {
        mContext = context;
    }

    public ProfileProblemFragment(Context context, CustomerRequest customer) {
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

        mSolution = (EditText) view.findViewById(R.id.create_edit_solution);
        mSolution.setEnabled(false);

        mNote = (EditText) view.findViewById(R.id.create_edit_note);
        mNote.setEnabled(false);

        mProduct = (EditText) view.findViewById(R.id.create_edit_product_need);
        mProduct.setEnabled(false);
    }

    private void onUpdate() {
        mProblem.setText("" + mCustomer.getProblemType());
        mSolution.setText(mCustomer.getSolution());
        mProduct.setText(mCustomer.getSuggestProduct());
    }
}
