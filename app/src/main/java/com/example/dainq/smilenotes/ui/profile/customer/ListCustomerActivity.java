package com.example.dainq.smilenotes.ui.profile.customer;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;


public class ListCustomerActivity extends AppCompatActivity implements OnSpinnerItemSelectedListener {

    private String TAG = "ListCustomerActivity";
    private SingleSpinnerLayout mSpinner;
    private TextView mTextNotResults;

    private CustomerAdapter mAdapter;
    private RealmController mRealmController;

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
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle(R.string.list_customer);
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView mListCustomer = (RecyclerView) findViewById(R.id.list_customer);
        mListCustomer.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListCustomer.setLayoutManager(layoutManager);

        mTextNotResults = (TextView) findViewById(R.id.list_not_results);

        // create an empty adapter and add it to the recycler view
        mAdapter = new CustomerAdapter(this);
        mListCustomer.setAdapter(mAdapter);
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
        Bundle mExtras = getIntent().getExtras();
        mRealmController = RealmController.with(this);
        if (mExtras != null) {
            mType = mExtras.getInt(Constant.CUSTOMER_LEVEL);
        }
        Log.d(TAG, "dainq mType List " + mType);
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
        RealmResults<CustomerObject> mRealmResults;
        if (position == 1) {
            //Calculator num day of month
            //get Current date
            Calendar calendar = Calendar.getInstance();
            Date end = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            String day = dateFormat.format(end);
            int temp = Integer.parseInt(day);

            //get start date before from today
            calendar.add(Calendar.DAY_OF_MONTH, -(temp - 1));
            Date start = calendar.getTime();

            Log.d(TAG, "dainq start/end day " + start + "  //  " + end);
            mRealmResults = mRealmController.queryedCustomers(Constant.CUSTOMER_DATE_CREATE, start, end);
        } else {
            mRealmResults = mRealmController.queryedCustomers(position);
        }
        Log.d(TAG, "dainq position spinner " + position);
        if (mRealmResults.isEmpty()) {
            mTextNotResults.setVisibility(View.VISIBLE);
        } else {
            mTextNotResults.setVisibility(View.GONE);
            setRealmAdapter(mRealmResults);
        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
