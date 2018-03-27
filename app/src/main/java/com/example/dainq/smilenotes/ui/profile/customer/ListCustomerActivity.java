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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.request.customer.ListCustomerRequest;
import com.example.dainq.smilenotes.model.response.customer.ListCustomerResponse;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

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

    //list customer
    private RecyclerView mListCustomer;

    //new adapter customer
    private CustomerAdapter mCustomerAdapter;

    //type of spinner
    private int mType;

    //interface customer
    private APICustomer mService;

    //session to manager, store information of user in Pref
    private SessionManager mSession;

    //data customer get from server
    private List<CustomerRequest> mData;

    //id of user, get from session
    private String mUserId;

    //token create by server help user do anything
    private String mToken;

    //progressbar
    private ProgressBar mProgressView;

    //list level
    private ArrayList<String> mListLevel;


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

        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);

        mListLevel = new ArrayList<String>();

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

        initRetrofit();

        mCustomerAdapter = new CustomerAdapter();
        mCustomerAdapter.setHasStableIds(true);
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

    /*
    * this funtion to pass data into adapter to show in recycler view
    * */
    private void getListCustomer() {
        Bundle mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mType = mExtras.getInt(Constant.CUSTOMER_LEVEL);
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
        mProgressView.setVisibility(View.VISIBLE);

        //get customers is level 0
        if (position == Constant.CUSTOMER_TYPE_NEW || position == Constant.CUSTOMER_TYPE_NEW_MONTH) {
            mListLevel.clear();
            mListLevel.add("0");

            ListCustomerRequest request = createDataRequest(mListLevel, mUserId);
            processGetListCustomer(request, false);
        }

        //get list customers in level 1 & 2
        if (position == Constant.CUSTOMER_TYPE_CONSUMER) {
            mListLevel.clear();
            mListLevel.add("1");
            mListLevel.add("2");

            ListCustomerRequest request = createDataRequest(mListLevel, mUserId);
            processGetListCustomer(request, false);
        }

        //get listcustomer in level 3 & 4
        if (position == Constant.CUSTOMER_TYPE_DISTRIBUTION) {
            mListLevel.clear();
            mListLevel.add("3");
            mListLevel.add("4");

            ListCustomerRequest request = createDataRequest(mListLevel, mUserId);
            processGetListCustomer(request, false);
        }

        //get all customer
        if (position == Constant.CUSTOMER_TYPE_ALL) {
            processGetListCustomer(null, true);
        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    protected void onResume() {
        //need notifyDataSetChange in here to update data view
        //todo
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
    private void processGetListCustomer(ListCustomerRequest request, boolean getAll) {
        Call<ListCustomerResponse> response;
        //if getAll -> get all customer, else get by level by user chosen
        if (getAll) {
            response = mService.getListCustomerByUserId(mUserId, mToken);
        } else {
            response = mService.getListCustomerByLevel(request, mToken);
        }

        response.enqueue(new Callback<ListCustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ListCustomerResponse> call, @NonNull Response<ListCustomerResponse> response) {
                //getbody of response from server
                ListCustomerResponse customerResponse = response.body();

                //hide progressbar
                mProgressView.setVisibility(View.INVISIBLE);

                //check response from server is null or not
                if (customerResponse != null) {
                    int code = customerResponse.getCode();
                    if (code == Constant.RESPONSE_SUCCESS) {
                        Log.d(TAG, "-->>[processGetList] onResponse: " + customerResponse.getCode() + " - " + customerResponse.getMessage());

                        //getInformation from json array
                        mData = customerResponse.getData();
                        mCustomerAdapter = new CustomerAdapter(ListCustomerActivity.this, mData);
                        mListCustomer.setAdapter(mCustomerAdapter);

                        Log.d(TAG, "-->[processGetList] lenght data: " + mData.size());
                    } else {
                        Log.d(TAG, "-->>[processGetList] onResponse: " + customerResponse.getCode() + " - " + customerResponse.getMessage());
                    }
                } else {
                    //TODO: do something when respone is null
                    Log.d(TAG, "-->[processGetlist] respone null!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListCustomerResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "-->>[processGetList] onFailure: " + t.getMessage() + " - " + t.getCause());

                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * create data request to get list
     */
    private ListCustomerRequest createDataRequest(ArrayList<String> listLevel, String idUser) {
        ListCustomerRequest request = new ListCustomerRequest();

        request.setUserId(idUser);
        request.setListLevel(listLevel);

        return request;
    }
}
