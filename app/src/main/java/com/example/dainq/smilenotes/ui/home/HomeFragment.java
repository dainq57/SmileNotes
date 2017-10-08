package com.example.dainq.smilenotes.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.controller.realm.RealmCustomerAdapter;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.example.dainq.smilenotes.ui.customer.CustomerAdapter;
import com.example.dainq.smilenotes.ui.customer.ListCustomerActivity;

import java.util.ArrayList;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class HomeFragment extends BaseFragment implements OnSpinnerItemSelectedListener {
    private Context mContext;
    private RecyclerView mListCustomer;
    private CustomerAdapter mAdapter;
    private SingleSpinnerLayout mSpinner;
    private RealmResults<CustomerObject> mRealmResult;
    private RealmController mRealmController;

    public HomeFragment(Context context) {
        mContext = context;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        final PieView pieView = (PieView) view.findViewById(R.id.pie_view);
        setPieView(pieView);

        mListCustomer = (RecyclerView) view.findViewById(R.id.home_list_customer);
        mListCustomer.setHasFixedSize(true);

        initSpinner(view);

        RealmCustomerAdapter realmAdapter = new RealmCustomerAdapter(mContext, mRealmResult, true);
        mAdapter = new CustomerAdapter(mContext);
        mListCustomer.setAdapter(mAdapter);

        mRealmController = RealmController.with(this);
        mRealmResult = mRealmController.getCustomers();
        mRealmResult = mRealmController.sortCustomerByDate("dateofbirth");
        mAdapter.setRealmAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setPieView(PieView pieView) {
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();
        pieHelperArrayList.add(new PieHelper(20, mContext.getResources().getColor(R.color.color_piechart_level_1, null)));
        pieHelperArrayList.add(new PieHelper(30, mContext.getResources().getColor(R.color.color_piechart_level_2, null)));
        pieHelperArrayList.add(new PieHelper(10, mContext.getResources().getColor(R.color.color_piechart_level_3, null)));
        pieHelperArrayList.add(new PieHelper(40, mContext.getResources().getColor(R.color.color_piechart_level_4, null)));

        pieView.setDate(pieHelperArrayList);
        pieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            @Override
            public void onPieClick(int index) {
                switch (index) {
                    case 0:
                        openListCustomer(Constant.CUSTOMER_TYPE_NEW);
                        break;
                    case 1:
                        openListCustomer(Constant.CUSTOMER_TYPE_NEW_MONTH);
                        break;
                    case 2:
                        openListCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
                        break;
                    case 3:
                        openListCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
                        break;
                }
            }
        });
    }

    private void openListCustomer(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_LEVEL_CUSTOMER, id);

        Intent intent = new Intent(mContext, ListCustomerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initSpinner(View view) {
        mSpinner = (SingleSpinnerLayout) view.findViewById(R.id.home_spinner_select_number);

        if (mSpinner != null) {
            String[] menuItem = getResources().getStringArray(R.array.array_spinner_home);
            mSpinner.setSpinnerList(SpinnerItem.getSpinnerItem(menuItem));
            mSpinner.setOnSpinnerItemSelectedListener(this);
            mSpinner.getSpinner().setDropDownHorizontalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_offset_vertical));
            mSpinner.setSelection(0);
        }
    }

    @Override
    public void onItemSelected(int position) {

    }

    @Override
    public void onNothingSelected() {

    }
}
