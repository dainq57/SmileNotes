package com.example.dainq.smilenotes.ui.profile.customer;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
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

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.controllers.api.APIUser;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.object.CustomerObject;
import com.example.dainq.smilenotes.model.request.CustomerRequest;
import com.example.dainq.smilenotes.model.request.UserRequest;
import com.example.dainq.smilenotes.model.response.ListCustomerResponse;
import com.example.dainq.smilenotes.ui.LoginActivity;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* every code by comment be using to stoge data in realm data, do not remove it
*
* */
public class ListCustomerActivity extends AppCompatActivity implements OnSpinnerItemSelectedListener {

    private String TAG = "ListCustomerActivity";
    private SingleSpinnerLayout mSpinner;
    private TextView mTextNotResults;

    //old adapter customer
//    private CustomerAdapter mAdapter;
//    private RealmController mRealmController;

    //list customer
    private RecyclerView mListCustomer;

    //new adapter customer
    private CCustomerAdapter mCustomerAdapter;

    //type of spinner
    private int mType;

    //interface customer
    private APICustomer mService;

    //session to manager, store information of user in Pref
    private SessionManager mSession;

    //data customer get from server
    private CustomerRequest[] mData;

    //id of user, get from session
    private String mUserId;

    //token create by server help user do anything
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        initView();
    }

    private void initView() {
        //create new session manager
        mSession = new SessionManager(ListCustomerActivity.this);

        //get userId and Token from session
        mUserId = mSession.getUserDetails().getId();
        mToken = mSession.getUserDetails().getToken();

        Log.d(TAG, "-->[initView] userId - token: " + mUserId + " - " + mToken);

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

    /*
    * config recycler view to show list customer
    * */
    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mListCustomer = (RecyclerView) findViewById(R.id.list_customer);
        mListCustomer.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListCustomer.setLayoutManager(layoutManager);

        mTextNotResults = (TextView) findViewById(R.id.list_not_results);

        // create an empty adapter and add it to the recycler view
//        mAdapter = new CustomerAdapter(this);
//        mAdapter.setHasStableIds(true);
//        mListCustomer.setAdapter(mAdapter);

        /*---create adapter, new adapter---*/

        //get list customer from server
        initRetrofit();

        mCustomerAdapter = new CCustomerAdapter();
        mCustomerAdapter.setHasStableIds(true);

        //getall customer
        processGetListCustomer(0, false);
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

//    public void setRealmAdapter(RealmResults<CustomerObject> customer) {
//        RealmCustomerAdapter realmAdapter = new RealmCustomerAdapter(this.getApplicationContext(), customer, true);
//        // Set the data and tell the RecyclerView to draw
//        mAdapter.setRealmAdapter(realmAdapter);
//        mAdapter.notifyDataSetChanged();
//    }


    /*
    * this funtion to pass data into adapter to show in recycler view
    * */
    private void getListCustomer() {
        Bundle mExtras = getIntent().getExtras();
//        mRealmController = RealmController.with(this);
        if (mExtras != null) {
            mType = mExtras.getInt(Constant.CUSTOMER_LEVEL);
        }
//        Log.d(TAG, "dainq mType List " + mType);
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
//        RealmResults<CustomerObject> mRealmResults;
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

//            Log.d(TAG, "dainq start/end day " + start + "  //  " + end);
//            mRealmResults = mRealmController.queryedCustomers(Constant.CUSTOMER_DATE_CREATE, start, end);
        } else {
//            mRealmResults = mRealmController.queryedCustomers(position);
        }
//        Log.d(TAG, "dainq position spinner " + position);

        //check list customer is Empty or not

        //TODO
//        if (true) {
//            mTextNotResults.setVisibility(View.VISIBLE);
//        } else {
//            mTextNotResults.setVisibility(View.GONE);
////            setRealmAdapter(mRealmResults);
//
//            //TODO set adatapter here
//        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    protected void onResume() {
//        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    /**
     * create service with retrofit*
     */
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL_CUSTOMER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(APICustomer.class);
    }

    /**
     * this funtion is process to get list customer by userId
     */
    private void processGetListCustomer(int level, boolean getAll) {
        Call<ListCustomerResponse> response;
        //if getAll -> get all customer, else get by level by user chosen
        if (!getAll) {
            response = mService.getListCustomerByUserId(mUserId, mToken);
        } else {
            response = mService.getListCustomerByLevel(mUserId, level, mToken);
        }

        response.enqueue(new Callback<ListCustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ListCustomerResponse> call, @NonNull Response<ListCustomerResponse> response) {
                //getbody of response from server
                ListCustomerResponse customerResponse = response.body();

                //check response from server is null or not
                if (customerResponse != null) {
                    Log.d(TAG, "-->>[processGetList] onResponse: " + customerResponse.getCode() + " - " + customerResponse.getMessage());

                    //getInformation from json array
                    mData = customerResponse.getData();
                    mCustomerAdapter = new CCustomerAdapter(ListCustomerActivity.this, mData);
                    mListCustomer.setAdapter(mCustomerAdapter);

                    Log.d(TAG, "-->[processGetList] lenght data: " + mData.length);
                } else {
                    //TODO: do something when respone is null
                    Log.d(TAG, "-->[processGetlist] respone null!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListCustomerResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "-->>[processGetList] onFailure: " + t.getMessage() + " - " + t.getCause());
            }
        });
    }
}
