package com.example.dainq.smilenotes.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.response.customer.LevelData;
import com.example.dainq.smilenotes.model.response.customer.NumberCustomerResponse;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.example.dainq.smilenotes.ui.profile.customer.ListCustomerActivity;
import com.example.dainq.smilenotes.ui.profile.product.ProductAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements OnSpinnerItemSelectedListener, View.OnClickListener {
    private String TAG = "HomeFragment";

    private Context mContext;
    private ProductAdapter mAdapter;

    private TextView mCPotentialMonth;
    private TextView mCPotential;
    private TextView mCConsumer;
    private TextView mCDistribution;
    private CircleImageView mAvatarUser;
    private TextView mNameUser;
    private TextView mTextNotResults;
    private SingleSpinnerLayout mSpinner;
    private PieView mPieView;

    private LinearLayout mLayoutSumary;
    private LinearLayout mLayoutPie;
    private TextView mTxtSumary;
    private TextView mTxtPie;

    private int potentialMonth;
    private int potential;
    private int consumer;
    private int distributor;

    //session of user
    private SessionManager mSession;

    //interface service
    private APICustomer mService;

    public HomeFragment(Context context, SessionManager session) {
        mContext = context;
        mSession = session;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_CUSTOMER);
        mService = retrofit.create(APICustomer.class);

        initView(view);
        processGetNumberCustomer();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(View view) {
        mPieView = (PieView) view.findViewById(R.id.pie_view);
        initSummaryView(view);

        RecyclerView mListCustomer = (RecyclerView) view.findViewById(R.id.home_list_customer);
        mListCustomer.setHasFixedSize(true);
        mListCustomer.setLayoutManager(new LinearLayoutManager(mContext));

//        mAdapter = new ProductAdapter(mContext);
//        mListCustomer.setAdapter(mAdapter);

        initSpinner(view);

        mTextNotResults = (TextView) view.findViewById(R.id.home_list_not_results);

        mTxtSumary = (TextView) view.findViewById(R.id.home_btn_sumary);
        mTxtSumary.setOnClickListener(this);
        mTxtPie = (TextView) view.findViewById(R.id.home_btn_pie);
        mTxtPie.setOnClickListener(this);

        mLayoutSumary = (LinearLayout) view.findViewById(R.id.home_summary);
        mLayoutPie = (LinearLayout) view.findViewById(R.id.home_pie_chart);
    }

    private void initSummaryView(View view) {
        LinearLayout customerPotentialMonth = (LinearLayout) view.findViewById(R.id.home_customer_potential_month);
        customerPotentialMonth.setOnClickListener(this);

        LinearLayout customerPotential = (LinearLayout) view.findViewById(R.id.home_customer_potential);
        customerPotential.setOnClickListener(this);

        LinearLayout customerConsumer = (LinearLayout) view.findViewById(R.id.home_customer_consumer);
        customerConsumer.setOnClickListener(this);

        LinearLayout customerDistribution = (LinearLayout) view.findViewById(R.id.home_customer_distribution);
        customerDistribution.setOnClickListener(this);

        mCPotentialMonth = (TextView) view.findViewById(R.id.home_customer_potential_month_number);
        mCPotential = (TextView) view.findViewById(R.id.home_customer_potential_number);
        mCConsumer = (TextView) view.findViewById(R.id.home_customer_consumer_number);
        mCDistribution = (TextView) view.findViewById(R.id.home_customer_distribution_number);

        mAvatarUser = (CircleImageView) view.findViewById(R.id.home_avatar_user);
        String pathAvatar = mSession.getUserDetails().getPathAvatar();

        //if path != null, set avatar for user
        if (pathAvatar != null) {
            mAvatarUser.setImageBitmap(Utility.decodeImage(pathAvatar));
        }

        mNameUser = (TextView) view.findViewById(R.id.home_name_user);

        //get name user from session afrer login
        mNameUser.setText(mSession.getUserDetails().getFullName());
    }

    private void setDataPieView(PieView pieView, int poten, int consum, int distribu) {
        int sum = poten + consum + distribu;
        float pote = 0, cons = 0, dis = 0;
        if (sum != 0) {
            pote = 100 * poten / sum;
            cons = 100 * consum / sum;
            dis = 100 * distribu / sum;
        }
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();
        pieHelperArrayList.add(new PieHelper(pote, mContext.getResources().getColor(R.color.color_piechart_level_1, null)));
        pieHelperArrayList.add(new PieHelper(cons, mContext.getResources().getColor(R.color.color_piechart_level_3, null)));
        pieHelperArrayList.add(new PieHelper(dis, mContext.getResources().getColor(R.color.color_piechart_level_4, null)));

        pieView.setDate(pieHelperArrayList);
        pieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            @Override
            public void onPieClick(int index) {
                switch (index) {
                    case 0:
                        openListCustomer(Constant.CUSTOMER_TYPE_NEW);
                        break;
                    case 1:
                        openListCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
                        break;
                    case 2:
                        openListCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
                        break;
                }
            }
        });
    }

    private void setDataSumary(int poten, int potenMonth, int consum, int distribu) {
        mCPotentialMonth.setText(potenMonth > 10 ? ("" + potenMonth) : ("0" + potenMonth));
        mCPotential.setText(poten > 10 ? ("" + poten) : ("0" + poten));
        mCConsumer.setText(consum > 10 ? ("" + consum) : ("0" + consum));
        mCDistribution.setText(distribu > 10 ? ("" + distribu) : ("0" + distribu));
    }

    private void openListCustomer(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.CUSTOMER_LEVEL, id);

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
        }
    }

    @Override
    public void onItemSelected(int position) {
        Log.d("dainq ", "spinner " + position);
        sortCustomerBetween(position);
    }

    @Override
    public void onNothingSelected() {
    }

    private void sortCustomerBetween(int type) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();

        String name = mSession.getUserDetails().getFullName();
        mNameUser.setText(name);

        sortCustomerBetween(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_customer_potential:
                openListCustomer(Constant.CUSTOMER_TYPE_NEW);
                break;

            case R.id.home_customer_potential_month:
                openListCustomer(Constant.CUSTOMER_TYPE_NEW_MONTH);
                break;

            case R.id.home_customer_consumer:
                openListCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
                break;

            case R.id.home_customer_distribution:
                openListCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
                break;

            case R.id.home_btn_pie:
                switchView(false, mTxtPie, mTxtSumary);
                break;

            case R.id.home_btn_sumary:
                switchView(true, mTxtSumary, mTxtPie);
                break;

            default:
                break;
        }
    }

    private void switchView(boolean value, TextView btnSwitch, TextView btnHide) {
        btnSwitch.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        btnHide.setTextColor(mContext.getResources().getColor(R.color.border_color));

        if (value) {
            mLayoutSumary.setVisibility(View.VISIBLE);
            mLayoutPie.setVisibility(View.GONE);
        } else {
            mLayoutSumary.setVisibility(View.GONE);
            mLayoutPie.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
     ** get number of customer by level
     */
    private void processGetNumberCustomer() {
        String userId = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        Call<NumberCustomerResponse> response = mService.getNumberCustomer(userId, token);
        response.enqueue(new Callback<NumberCustomerResponse>() {
            @Override
            public void onResponse(Call<NumberCustomerResponse> call, Response<NumberCustomerResponse> response) {
                NumberCustomerResponse serverResponse = response.body();

                if (serverResponse != null) {
                    Log.d(TAG, "-->[process-get-number-customer] onResponse sucess: " + serverResponse.getCode() + ": " + serverResponse.getMessage());

                    LevelData numberCustomer = serverResponse.getData();
                    potential = numberCustomer.getPotential();
                    potentialMonth = numberCustomer.getPotentialMonth();
                    consumer = numberCustomer.getConsumer();
                    distributor = numberCustomer.getDistributor();

                    //set number text in home screen
                    setDataSumary(potential, potentialMonth, consumer, distributor);

                    //set data of pieView in home screen
                    setDataPieView(mPieView, potential, consumer, distributor);
                } else {
                    Log.d(TAG, "-->[process-get-number-customer] onResponse: " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NumberCustomerResponse> call, Throwable t) {
                Log.d(TAG, "-->[process-get-number-customer] onFailure: " + t.getMessage());
            }
        });
    }
}