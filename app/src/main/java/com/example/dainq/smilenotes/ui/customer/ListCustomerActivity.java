package com.example.dainq.smilenotes.ui.customer;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.controller.realm.RealmCustomerAdapter;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class ListCustomerActivity extends AppCompatActivity implements OnSpinnerItemSelectedListener {
    private Toolbar mToolbar;
    private CustomerAdapter mAdapter;
    private RecyclerView mListCustomer;

    private SingleSpinnerLayout mSpinner;
    private String mTitle;

    private Bundle mExtras;
    private RealmController mRealmController;
    private RealmResults<CustomerObject> mRealmResults;

    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        initView();
    }

    private void initView() {
        initToolBar();
        initSpinner();
        setupRecycler();
        getListCustomer();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle("DANH SÁCH");
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mListCustomer = (RecyclerView) findViewById(R.id.list_customer);
        mListCustomer.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListCustomer.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        mAdapter = new CustomerAdapter(this);
        mListCustomer.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRealmAdapter(RealmResults<CustomerObject> customer) {
        RealmCustomerAdapter realmAdapter = new RealmCustomerAdapter(this.getApplicationContext(), customer, true);
        // Set the data and tell the RecyclerView to draw
        mAdapter.setRealmAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void getListCustomer() {
        mExtras = getIntent().getExtras();
        mRealmController = RealmController.with(this);
        if (mExtras != null) {
            mType = mExtras.getInt(Constant.KEY_LEVEL_CUSTOMER);
        }
        mSpinner.setSelection(mType);
    }

    private void initSpinner() {
        mSpinner = (SingleSpinnerLayout) findViewById(R.id.list_spinner_filter);

        if (mSpinner != null) {
            String[] menuItem = getResources().getStringArray(R.array.array_spinner_list_customer);
            mSpinner.setSpinnerList(SpinnerItem.getSpinnerItem(menuItem));
            mSpinner.setOnSpinnerItemSelectedListener(this);
            mSpinner.getSpinner().setDropDownHorizontalOffset(getResources().getDimensionPixelSize(R.dimen.dropdown_offset_vertical));
        }
    }

    @Override
    public void onItemSelected(int position) {
        if (mRealmController.hasCustomers()) {
            mRealmResults = mRealmController.queryedCustomers(position);
            setRealmAdapter(mRealmResults);
        }
    }

    @Override
    public void onNothingSelected() {
    }
}
