package com.example.dainq.smilenotes.ui.profile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.response.customer.CustomerResponse;
import com.example.dainq.smilenotes.ui.create.CreateActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ProfileActivity";

    private CustomerRequest mCustomer;

    private TextView mAda;
    private RatingBar mRating;
    private TextView mDateCreate;
    private CircleImageView mAvatar;

    //progress view
    private ProgressBar mProgressView;

    //type tab open profile
    private int type;

    //session of User
    private SessionManager mSession;

    //interface api of customer
    private APICustomer mService;

    //id of customer
    private String mIdCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initRetrofit();
        initView();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL_CUSTOMER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(APICustomer.class);
    }

    private void initView() {
        //create new session of customer
        mSession = new SessionManager(this);

        mAda = (TextView) findViewById(R.id.profile_ada);
        mRating = (RatingBar) findViewById(R.id.profile_rating);

        FrameLayout mButtonEdit = (FrameLayout) findViewById(R.id.profile_btn_edit);
        mButtonEdit.setOnClickListener(this);

        mDateCreate = (TextView) findViewById(R.id.profile_date_create);
        mAvatar = (CircleImageView) findViewById(R.id.profile_avatar);
        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void onUpdate() {
        //show progress bar
        mProgressView.setVisibility(View.VISIBLE);

        //call get infomation of customer
        processGetInfoCustomer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //call this in onResume to get new update data after edit, and back to activity profile
        //from activity edit/create
        onUpdate();
        initToolBar();
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
    funtion to get information of customer
    */
    private void processGetInfoCustomer() {
        Bundle extras = getIntent().getExtras();
        String idCustomer = extras.getString(Constant.KEY_ID);
        type = extras.getInt(Constant.KEY_TYPE_PRODILE);

        String token = mSession.getUserDetails().getToken();
        String idUser = mSession.getUserDetails().getId();

        Call<CustomerResponse> response = mService.getCustomer(idUser, idCustomer, token);
        response.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);

                //getbody of response
                CustomerResponse serverRespone = response.body();

                if (serverRespone != null) {
                    mCustomer = serverRespone.getData();
                    mIdCustomer = mCustomer.getId();

                    Log.d(TAG, "-->[get-info] response: " + serverRespone.getCode() + " - " + serverRespone.getMessage());
                    Log.d(TAG, "-->[get-info] id: " + serverRespone.getData().getId());

                    initProfileBanner(mCustomer);

                    initViewPager(mCustomer);
                } else {
                    Log.d(TAG, "-->[get-info] response: data null");
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d(TAG, "-->[failure] mess: " + t.getMessage());
            }
        });
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
    }

    //set header profile
    private void initProfileBanner(CustomerRequest customer) {
        //set title in toolbar
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(customer.getName());

        //check level of customer to set ada view or invisible
        if (customer.getLevel() > Constant.CUSTOMER_LEVEL_2) {
            String ada = customer.getAdaCode();
            if (ada == null) {
                mAda.setText(getResources().getString(R.string.ada_code, ""));
            } else {
                mAda.setText(getResources().getString(R.string.ada_code, mCustomer.getAdaCode()));
            }
            mAda.setVisibility(View.VISIBLE);
        } else {
            mAda.setVisibility(View.GONE);
        }

        //set level
        mRating.setRating(customer.getLevel() + 1);
        Log.d(TAG, "-->[init-profile] level: " + customer.getLevel());

        //get dateCreate and set
        String date = customer.getCreateDate();
        mDateCreate.setText(getResources().getString(R.string.date_create, date));
        Log.d(TAG, "-->[init-profile] create-date: " + date);

        //get avatar
        String avatar = customer.getPathAvatar();
        if (avatar != null) {
            mAvatar.setImageBitmap(Utility.decodeImage(avatar));
        }
    }

    private void initViewPager(CustomerRequest customer) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager, customer);
        if (type == Constant.PROFILE_TYPE_PRODUCT) {
            viewPager.setCurrentItem(3);
        }
        if (type == Constant.PROFILE_TYPE_PLAN) {
            viewPager.setCurrentItem(2);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager, CustomerRequest customer) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileInfoFragment(this, customer), getString(R.string.tab_info));
        adapter.addFragment(new ProfileProblemFragment(this, customer), getString(R.string.tab_problem));
//        adapter.addFragment(new ProfilePlanFragment(this, customer), getString(R.string.tab_plan));

        //if customer is level biggest than lv0, add product fragment
        ProfileProductFragment fragmentProduct = new ProfileProductFragment(this, customer);
        if (mCustomer.getLevel() > Constant.CUSTOMER_LEVEL_0) {
            adapter.addFragment(fragmentProduct, getString(R.string.tab_product));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_btn_edit:
                startEdit(mIdCustomer);
                break;
            default:
                break;
        }
    }

    /**
     * start edit customer with idCustomer
     */
    private void startEdit(String idCustomer) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ACTION, Constant.ACTION_EDIT);
        bundle.putString(Constant.KEY_ID, idCustomer);

        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
