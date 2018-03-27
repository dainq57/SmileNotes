package com.example.dainq.smilenotes.ui.search;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.request.customer.SearchRequest;
import com.example.dainq.smilenotes.model.response.customer.SearchData;
import com.example.dainq.smilenotes.model.response.customer.SearchResponse;
import com.example.dainq.smilenotes.ui.profile.customer.CustomerAdapter;

import java.util.List;

import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private String TAG = "SearchActivity";

    private SearchView mSearch;
    private RecyclerView mListResult;
    private TextView mTextNotResult;
    private CustomerAdapter mAdapter;

    //session
    private SessionManager mSession;

    //interface service api
    private APICustomer mService;

    //progress view
    private ProgressBar mProgressView;

    //data get from server
    private SearchData mData;

    //list customer from serrver
    private List<CustomerRequest> mListCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //create retrofit customer
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_CUSTOMER);
        mService = retrofit.create(APICustomer.class);

        //create session of user
        mSession = new SessionManager(SearchActivity.this);

        initView();
    }

    private void initView() {
        initToolBar();

        mSearch = (SearchView) findViewById(R.id.search);
        mSearch.setIconifiedByDefault(true);
        mSearch.setOnQueryTextListener(mOnQueryTextListener);

        mListResult = (RecyclerView) findViewById(R.id.search_list);
        mListResult.setHasFixedSize(true);
        mListResult.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomerAdapter();
        mListResult.setAdapter(mAdapter);

        mTextNotResult = (TextView) findViewById(R.id.search_not_results);

        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle("TÌM KIẾM");

    }

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.length() > 0 && !TextUtils.isEmpty(query)) {
                //show progress
                mProgressView.setVisibility(View.VISIBLE);

                //call process search
                processSearchCustomer(query);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

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
     ** process search customer
     */
    private void processSearchCustomer(String keyword) {
        //get token of customer
        final String token = mSession.getUserDetails().getToken();

        //get id customer
        String idCustomer = mSession.getUserDetails().getId();

        //create search request
        SearchRequest request = new SearchRequest();

        //setdata to request
        request.setUserId(idCustomer);
        request.setKeyword(keyword);

        Call<SearchResponse> response = mService.searchCustomer(request, token);
        response.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);

                SearchResponse searchResponse = response.body();
                if (searchResponse != null) {
                    int code = searchResponse.getCode();

                    if (code == Constant.RESPONSE_SUCCESS) {
                        //get data in response
                        mData = searchResponse.getData();

                        //get list customer in data
                        mListCustomer = mData.getPageItems();

                        mAdapter = new CustomerAdapter(SearchActivity.this, mListCustomer);
                        mListResult.setAdapter(mAdapter);

                        int total = mListCustomer.size();
                        Log.d(TAG, "-->[process-search] respone success: " + total);
                        //check has result or not
                        if (total != 0) {
                            mListResult.setVisibility(View.VISIBLE);
                            mTextNotResult.setVisibility(View.INVISIBLE);
                        } else {
                            mListResult.setVisibility(View.INVISIBLE);
                            mTextNotResult.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "-->[process-search] respone: " + searchResponse.getCode() + ":" + searchResponse.getMessage());
                    }
                } else {
                    Log.d(TAG, "-->[process-search] respone null!");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);

                Log.d(TAG, "-->[process-search] failure!" + t.getMessage());
            }
        });

    }

    /*
     ** set data after search
     */

    private void setListData() {

    }
}
